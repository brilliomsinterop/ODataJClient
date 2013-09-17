/*
 * Copyright 2013 MS OpenTech.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.msopentech.odatajclient.proxy;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import com.msopentech.odatajclient.proxy.api.AsyncCall;
import com.msopentech.odatajclient.proxy.api.Query;
import com.msopentech.odatajclient.proxy.defaultservice.microsoft.test.odata.services.astoriadefaultservice.types.Employee;
import com.msopentech.odatajclient.proxy.defaultservice.microsoft.test.odata.services.astoriadefaultservice.types.EmployeeCollection;
import com.msopentech.odatajclient.proxy.defaultservice.microsoft.test.odata.services.astoriadefaultservice.types.Product;
import com.msopentech.odatajclient.proxy.defaultservice.microsoft.test.odata.services.astoriadefaultservice.types.ProductCollection;
import com.msopentech.odatajclient.proxy.defaultservice.microsoft.test.odata.services.astoriadefaultservice.types.SpecialEmployee;
import com.msopentech.odatajclient.proxy.defaultservice.microsoft.test.odata.services.astoriadefaultservice.types.SpecialEmployeeCollection;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import org.junit.Test;

public class AsyncTestITCase extends AbstractTest {

    @Test
    public void retrieveEntitySet() throws InterruptedException, ExecutionException {
        final Future<ProductCollection> futureProds = new AsyncCall<ProductCollection>() {

            @Override
            public ProductCollection call() {
                return container.getProduct().getAll();
            }
        };
        assertNotNull(futureProds);

        while (!futureProds.isDone()) {
        }

        final ProductCollection products = futureProds.get();
        assertNotNull(products);
        assertFalse(products.isEmpty());
        for (Product product : products) {
            assertNotNull(product);
        }
    }

    @Test
    public void updateEntity() throws InterruptedException, ExecutionException {
        final String random = UUID.randomUUID().toString();

        final Product product = container.getProduct().get(-10);
        product.setDescription("AsyncTest#updateEntity " + random);

        final Future<Void> futureFlush = new AsyncCall<Void>() {

            @Override
            public Void call() {
                container.flush();
                return null;
            }
        };
        assertNotNull(futureFlush);

        while (!futureFlush.isDone()) {
        }

        final Future<Product> futureProd = new AsyncCall<Product>() {

            @Override
            public Product call() {
                return container.getProduct().get(-10);
            }
        };

        assertEquals("AsyncTest#updateEntity " + random, futureProd.get().getDescription());
    }

    @Test
    public void polymorphQuery() throws Exception {
        final Future<Query<Employee, EmployeeCollection>> queryEmployee =
                new AsyncCall<Query<Employee, EmployeeCollection>>() {

            @Override
            public Query<Employee, EmployeeCollection> call() {
                return container.getPerson().createQuery(EmployeeCollection.class);
            }
        };

        assertEquals(7, queryEmployee.get().getResult().size());

        final Future<Query<SpecialEmployee, SpecialEmployeeCollection>> querySpecialEmployee =
                new AsyncCall<Query<SpecialEmployee, SpecialEmployeeCollection>>() {

            @Override
            public Query<SpecialEmployee, SpecialEmployeeCollection> call() {
                return container.getPerson().createQuery(SpecialEmployeeCollection.class);
            }
        };

        assertEquals(4, querySpecialEmployee.get().getResult().size());
    }
}
