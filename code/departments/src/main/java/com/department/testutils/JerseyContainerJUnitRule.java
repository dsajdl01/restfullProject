package com.department.testutils;
import org.glassfish.jersey.test.JerseyTest;
import org.glassfish.jersey.test.jetty.JettyTestContainerFactory;
import org.glassfish.jersey.test.spi.TestContainerException;
import org.glassfish.jersey.test.spi.TestContainerFactory;
import org.junit.rules.ExternalResource;

import javax.ws.rs.core.Application;

/**
 * Created by david on 21/11/16.
 */


/**
 * A simple wrapper around the JerseyTest class that allows the same test container to be used
 * for a full suite of tests, rather than start and stop the container for every test.
 *
 * Extend this class in the same way you would have extended JerseyTest, and override the configure() and getTestContainerFactory()
 * methods as you would with JerseyTest.
 *
 * Any other methods that need to be exposed from JerseyTest may need to be added to this class.
 *
 * This should be used with the {@link org.junit.ClassRule @ClassRule} annotation
 *
 *
 */public abstract class JerseyContainerJUnitRule extends ExternalResource {

    private JerseyTest staticJerseyTestInstance;

    private JerseyTest jerseyTestInstance = new JerseyTest() {
        @Override
        protected TestContainerFactory getTestContainerFactory() throws TestContainerException {
            return JerseyContainerJUnitRule.this.getTestContainerFactory();
        };

        protected Application configure() {
            return JerseyContainerJUnitRule.this.configure();
        };

    };

    /* See JerseyTest.getTestContainerFactory */
    // This sets up an in-memory test server that we deploy to with the configuration from the configure() method
    // This is the default - you can override this method to supply a different container if you wish.
    public TestContainerFactory getTestContainerFactory() {
        return new JettyTestContainerFactory();
    }

    /* See JerseyTest.configure */
    public abstract Application configure();

    @Override
    protected void before() throws Throwable {
        if (staticJerseyTestInstance == null) {
            jerseyTestInstance.setUp();
            staticJerseyTestInstance = jerseyTestInstance;
        }
    };

    @Override
    protected void after() {
        System.out.println("Stopping Jetty test web container");
        // release staticJerseyTestInstance so the next class sets it up from scratch
        try {
            staticJerseyTestInstance.tearDown();
        } catch (Exception e) {
            e.printStackTrace(); // NOSONAR
        }
        staticJerseyTestInstance = null;
    };

}
