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
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import com.msopentech.odatajclient.engine.data.ODataTimestamp;
import com.msopentech.odatajclient.engine.data.metadata.edm.geospatial.Geospatial;
import com.msopentech.odatajclient.engine.data.metadata.edm.geospatial.Geospatial.Type;
import com.msopentech.odatajclient.engine.data.metadata.edm.geospatial.MultiLineString;
import com.msopentech.odatajclient.engine.data.metadata.edm.geospatial.Point;
import com.msopentech.odatajclient.proxy.microsoft.test.odata.services.astoriadefaultservice.types.AllSpatialTypes;
import com.msopentech.odatajclient.proxy.microsoft.test.odata.services.astoriadefaultservice.types.ComputerDetail;
import com.msopentech.odatajclient.proxy.microsoft.test.odata.services.astoriadefaultservice.types.ConcurrencyInfo;
import com.msopentech.odatajclient.proxy.microsoft.test.odata.services.astoriadefaultservice.types.Customer;
import com.msopentech.odatajclient.proxy.microsoft.test.odata.services.astoriadefaultservice.types.CustomerInfo;
import com.msopentech.odatajclient.proxy.microsoft.test.odata.services.astoriadefaultservice.types.Message;
import com.msopentech.odatajclient.proxy.microsoft.test.odata.services.astoriadefaultservice.types.MessageKey;
import com.msopentech.odatajclient.proxy.microsoft.test.odata.services.astoriadefaultservice.types.Order;
import com.msopentech.odatajclient.proxy.microsoft.test.odata.services.astoriadefaultservice.types.OrderCollection;
import java.util.Collection;
import org.junit.Ignore;
import org.junit.Test;

/**
 * This is the unit test class to check entity retrieve operations.
 */
public class EntityRetrieveTestITCase extends AbstractTest {

    @Test
    public void exists() {
        assertTrue(container.getPerson().exists(-10));
        assertFalse(container.getPerson().exists(-11));
    }

    @Test
    public void read() {
        readCustomer(container, -10);
    }

    @Test
    public void navigate() {
        final Order order = container.getOrder().get(-9);
        assertNotNull(order);
        assertEquals(Integer.valueOf(-9), order.getOrderId());

        final ConcurrencyInfo concurrency = order.getConcurrency();
        assertNotNull(concurrency);
        assertEquals("2012-02-12T11:32:50.5072026", concurrency.getQueriedDateTime().toString());
        assertEquals(Integer.valueOf(78), order.getCustomerId());
    }

    @Test
    public void withGeospatial() {
        final AllSpatialTypes allSpatialTypes = container.getAllGeoTypesSet().get(-10);
        assertNotNull(allSpatialTypes);
        assertEquals(Integer.valueOf(-10), allSpatialTypes.getId());

        final MultiLineString geogMultiLine = allSpatialTypes.getGeogMultiLine();
        assertNotNull(geogMultiLine);
        assertEquals(Type.MULTILINESTRING, geogMultiLine.getType());
        assertEquals(Geospatial.Dimension.GEOGRAPHY, geogMultiLine.getDimension());
        assertFalse(geogMultiLine.isEmpty());

        final Point geogPoint = allSpatialTypes.getGeogPoint();
        assertNotNull(geogPoint);
        assertEquals(Type.POINT, geogPoint.getType());
        assertEquals(Geospatial.Dimension.GEOGRAPHY, geogPoint.getDimension());
        assertEquals(52.8606, geogPoint.getY(), 0);
        assertEquals(173.334, geogPoint.getX(), 0);
    }

    // TODO: test for lazy and eager
    @Test
    public void withInlineEntry() {
        final Customer customer = readCustomer(container, -10);
        final CustomerInfo customerInfo = customer.getInfo();
        assertNotNull(customerInfo);
        assertEquals(Integer.valueOf(11), customerInfo.getCustomerInfoId());
    }

    // TODO: test for lazy and eager
    @Test
    @Ignore
    public void withInlineFeed() {
        final Customer customer = readCustomer(container, -10);
        final OrderCollection orders = customer.getOrders();
        assertFalse(orders.isEmpty());
    }

    @Test
    public void withActions() {
        final ComputerDetail computerDetail = container.getComputerDetail().get(-10);
        assertEquals(Integer.valueOf(-10), computerDetail.getComputerDetailId());

        try {
            assertNotNull(ComputerDetail.class.getMethod("resetComputerDetailsSpecifications",
                    Collection.class, ODataTimestamp.class));
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void multiKey() {
        final MessageKey messageKey = new MessageKey();
        messageKey.setFromUsername("1");
        messageKey.setMessageId(-10);

        final Message message = container.getMessage().get(messageKey);
        assertNotNull(message);
        assertEquals("1", message.getFromUsername());
    }
}