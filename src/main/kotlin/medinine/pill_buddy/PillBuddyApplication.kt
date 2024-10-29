package medinine.pill_buddy

import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableJpaAuditing
@EnableScheduling
class PillBuddyApplication

inline val <reified T> T.log : Logger
	get() = LogManager.getLogger()

fun main(args: Array<String>) {
	runApplication<PillBuddyApplication>(*args)
}
