/*
 * Copyright 2015 dorkbox, llc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package dorkbox.messageBus.dispatch;


import dorkbox.messageBus.error.ErrorHandler;
import dorkbox.messageBus.publication.Publisher;
import dorkbox.messageBus.subscription.SubscriptionManager;

/**
 * @author dorkbox, llc
 *         Date: 2/2/15
 */
public interface Dispatch {
    void publish(Publisher publisher, ErrorHandler errorHandler, SubscriptionManager subscriptionManager, Object message1);
    void publish(Publisher publisher, ErrorHandler errorHandler, SubscriptionManager subscriptionManager, Object message1, Object message2);
    void publish(Publisher publisher, ErrorHandler errorHandler, SubscriptionManager subscriptionManager, Object message1, Object message2, Object message3);
}
