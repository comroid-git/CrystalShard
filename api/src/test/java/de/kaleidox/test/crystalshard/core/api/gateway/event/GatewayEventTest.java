package de.kaleidox.test.crystalshard.core.api.gateway.event;

import de.comroid.crystalshard.core.api.gateway.event.GatewayEventBase;

import org.junit.Test;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

public class GatewayEventTest {
    @Test
    public void testAllDeclareNameField() throws Exception {
        Reflections reflections = new Reflections(new ConfigurationBuilder()
                .setScanners(new SubTypesScanner())
                .addUrls(ClasspathHelper.forClass(GatewayEventBase.class))
        );

        int c = 0;

        for (Class<? extends GatewayEventBase> klass : reflections.getSubTypesOf(GatewayEventBase.class)) {
            c++;

            try {
                if (klass.getField("NAME").get(null) == null)
                    throw new NullPointerException();
            } catch (Throwable t) {
                throw new Exception("Field NAME not implemented in class: " + klass, t);
            }
        }

        System.out.println("Scanned " + c + " classes");
    }
}
