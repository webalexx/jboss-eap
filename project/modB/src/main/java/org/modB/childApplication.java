package org.modB;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class childApplication extends Application<childConfiguration> {

    public static void main(final String[] args) throws Exception {
        new childApplication().run(args);
    }

    @Override
    public String getName() {
        return "child";
    }

    @Override
    public void initialize(final Bootstrap<childConfiguration> bootstrap) {
        // TODO: application initialization
    }

    @Override
    public void run(final childConfiguration configuration,
                    final Environment environment) {
        // TODO: implement application
    }

}
