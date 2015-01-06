package com.tomgames.pit.client;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import com.tomgames.pit.PIT;

public class HtmlLauncher extends GwtApplication {

        @Override
        public GwtApplicationConfiguration getConfig () {
                return new GwtApplicationConfiguration(700, 500);
        }

        @Override
        public ApplicationListener getApplicationListener () {
                return new PIT();
        }
}