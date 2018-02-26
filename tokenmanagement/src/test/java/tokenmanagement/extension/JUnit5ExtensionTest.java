/*
 * JBoss, Home of Professional Open Source
 * Copyright 2017, Red Hat, Inc., and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package tokenmanagement.extension;

import javax.inject.Inject;

import org.jboss.weld.junit5.WeldJunit5Extension;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import tokenmanagement.common.BarBean;
import tokenmanagement.common.FooBean;
import tokenmanagement.common.MyQualifier;
import tokenmanagement.common.SomeBean;

/**
 * Basic test for JUnit 5 injection into parameter/field handled by Weld
 * @author <a href="mailto:manovotn@redhat.com">Matej Novotny</a>
 */
@ExtendWith(WeldJunit5Extension.class)
public class JUnit5ExtensionTest {
//
//    @Inject
//    SomeBean bean;

    @Test
    public void testFieldInjection() {

        // assert field injection works
    /*    Assertions.assertNotNull(bean);
       bean.ping();  */ 
    }

//    @Test
//    public void testparamInjection(FooBean foo) {
//        // assert param injection works
//        Assertions.assertNotNull(foo);
//        foo.ping();
//    }
//
//    @Test
//    public void testparamInjectionWithQualifier(@MyQualifier BarBean bar) {
//        // assert param injection with qualifier works
//        Assertions.assertNotNull(bar);
//        bar.ping();
//    }
}
