import cats.implicits._
import zio._
import zio.clock.Clock
import zio.interop.catz._
import zio.interop.catz.implicits._

import scala.concurrent.duration._
import fs2._
import fs2.kafka._

object Reproduction extends zio.App {

  val producerSettings: ProducerSettings[Task, String, String] =
    ProducerSettings[Task, String, String]
      .withBootstrapServers("localhost:9092")

  val consumerSettings: ConsumerSettings[Task, String, String] = ConsumerSettings[Task, String, String](
    keyDeserializer = Deserializer[Task, String],
    valueDeserializer = Deserializer[Task, String]
  ).withAutoOffsetReset(AutoOffsetReset.Latest)
    .withBootstrapServers("localhost:9092")
    .withGroupId("group")

  def run(args: List[String]): ZIO[ZEnv, Nothing, ExitCode] =
    (for {
      _ <- consumer(consumerSettings).flatMap(consumeProcess)
      _ <- producer(producerSettings).flatMap(produceProcess)
    } yield ()).useForever.exitCode

  def consumeProcess(consumer: KafkaConsumer[Task, String, String]): ZManaged[Clock, Throwable, Unit] = {
    val process = Stream.eval(consumer.subscribeTo("test")) ++
      consumer.stream
        .evalMap(record => Task(println(s"Received record $record")))

    process.compile.drain.forkManaged.unit
  }

  def produceProcess(producer: KafkaProducer[Task, String, String]): ZManaged[Clock, Throwable, Unit] =
    Stream
      .awakeEvery[Task](100.milliseconds)
      .evalMap(_ => producer.produce(ProducerRecords.one(ProducerRecord("test", "", "test"))))
      .compile
      .drain
      .forkManaged
      .unit

  def producer[K, V](producerSettings: ProducerSettings[Task, K, V]): ZManaged[Clock, Throwable, KafkaProducer[Task, K, V]] =
    withConcurrentEffect(implicit runtime => producerResource(producerSettings).toManaged)

  def consumer[K, V](consumerSettings: ConsumerSettings[Task, K, V]): ZManaged[Clock, Throwable, KafkaConsumer[Task, K, V]] =
    withConcurrentEffect(implicit runtime => consumerResource(consumerSettings).toManaged)

  def withConcurrentEffect[R <: Clock, E, A](f: Runtime[Clock] => ZManaged[R, E, A]): ZManaged[R, E, A] =
    ZIO.runtime[Clock].toManaged_.flatMap(f)
}
