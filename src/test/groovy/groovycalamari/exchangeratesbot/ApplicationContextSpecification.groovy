package groovycalamari.exchangeratesbot

import io.micronaut.context.ApplicationContext
import spock.lang.Shared
import spock.lang.Specification

abstract class ApplicationContextSpecification extends Specification {

    @Shared
    ApplicationContext applicationContext = ApplicationContext.run()
}