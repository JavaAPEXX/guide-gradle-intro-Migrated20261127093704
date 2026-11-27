// tag::copyright[]
/*******************************************************************************
 * Copyright (c) 2017, 2024 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
// end::copyright[]
package io.openliberty.guides.hello.it;

// tag::import[]
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.HttpStatus;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
// end::import[]

// tag::endpointit[]
public class EndpointIT {
    private static String webURL;

    @BeforeAll
    // tag::init[]
    public static void init() {
        String port = System.getProperty("http.port");
        String context = System.getProperty("context.root");
        webURL = "http://localhost:" + port + "/" + context + "/" + "servlet";
        System.out.println("URL: " + webURL);
    }
    // end::init[]

    // tag::test[]
    @Test
    // end::test[]
    public void testServlet() throws Exception {

        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpGet httpGet = new HttpGet(webURL);
            
            // tag::try[]
            try (CloseableHttpResponse response = client.execute(httpGet)) {
                int statusCode = response.getCode();
                assertEquals(HttpStatus.SC_OK, statusCode, "HTTP GET failed");

                StringBuilder builder = new StringBuilder();
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                                            response.getEntity().getContent(), StandardCharsets.UTF_8))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        builder.append(line);
                    }
                }
                
                assertTrue(builder.toString().contains("Hello! Is Gradle working for you?"),
                    "Unexpected response body: " + builder.toString());
            }
            // end::try[]
        }
    }
}
//end::endpointit[]