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
package dorkbox.util.messagebus;

import dorkbox.util.messagebus.error.DefaultErrorHandler;
import dorkbox.util.messagebus.error.ErrorHandlingSupport;
import dorkbox.util.messagebus.publication.Publisher;
import dorkbox.util.messagebus.publication.PublisherExact;
import dorkbox.util.messagebus.publication.PublisherExactWithSuperTypes;
import dorkbox.util.messagebus.publication.PublisherExactWithSuperTypesAndVarity;
import dorkbox.util.messagebus.subscription.SubscriptionManager;
import dorkbox.util.messagebus.subscription.WriterDistruptor;
import dorkbox.util.messagebus.synchrony.AsyncDisruptor;
import dorkbox.util.messagebus.synchrony.Sync;
import dorkbox.util.messagebus.synchrony.Synchrony;

/**
 * The base class for all message bus implementations with support for asynchronous message dispatch.
 *
 * See this post for insight on how it operates:  http://psy-lob-saw.blogspot.com/2012/12/atomiclazyset-is-performance-win-for.html
 * tldr; we use single-writer-principle + Atomic.lazySet
 *
 * @author dorkbox, llc
 *         Date: 2/2/15
 */
public
class MessageBus implements IMessageBus {
    private final ErrorHandlingSupport errorHandler;

    private final WriterDistruptor subscriptionWriter;

    private final SubscriptionManager subscriptionManager;

    private final Publisher publisher;
    private final Synchrony syncPublication;
    private final Synchrony asyncPublication;


    /**
     * By default, will permit subTypes and Varity Argument matching, and will use half of CPUs available for dispatching async messages
     */
    public
    MessageBus() {
        this(Runtime.getRuntime().availableProcessors()/2);
    }

    /**
     * By default, will permit subTypes and Varity Argument matching
     *
     * @param numberOfThreads how many threads to use for dispatching async messages
     */
    public
    MessageBus(int numberOfThreads) {
//        this(PublishMode.ExactWithSuperTypesAndVarity, numberOfThreads);
        this(PublishMode.ExactWithSuperTypes, numberOfThreads);
    }

    /**
     * By default, will use half of CPUs available for dispatching async messages
     *
     * @param publishMode     Specifies which publishMode to operate the publication of messages.
     */
    public
    MessageBus(final PublishMode publishMode) {
        this(publishMode, Runtime.getRuntime().availableProcessors());
    }
    /**
     * @param publishMode     Specifies which publishMode to operate the publication of messages.
     * @param numberOfThreads how many threads to use for dispatching async messages
     */
    public
    MessageBus(final PublishMode publishMode, int numberOfThreads) {
        // round to the nearest power of 2
        numberOfThreads = 1 << (32 - Integer.numberOfLeadingZeros(getMinNumberOfThreads(numberOfThreads) - 1));

        this.errorHandler = new DefaultErrorHandler();

        /**
         * Will subscribe and publish using all provided parameters in the method signature (for subscribe), and arguments (for publish)
         */
        this.subscriptionManager = new SubscriptionManager(numberOfThreads, errorHandler);

        subscriptionWriter = new WriterDistruptor(errorHandler, subscriptionManager);


        switch (publishMode) {
            case Exact:
                publisher = new PublisherExact(errorHandler, subscriptionManager);
                break;

            case ExactWithSuperTypes:
                publisher = new PublisherExactWithSuperTypes(errorHandler, subscriptionManager);
                break;

            case ExactWithSuperTypesAndVarity:
            default:
                publisher = new PublisherExactWithSuperTypesAndVarity(errorHandler, subscriptionManager);
        }

        syncPublication = new Sync();
//        asyncPublication = new PubAsync(numberOfThreads, errorHandler, publisher, syncPublication);
        asyncPublication = new AsyncDisruptor(numberOfThreads, errorHandler, publisher, syncPublication);
    }

    /**
     * Always return at least 2 threads
     */
    private static
    int getMinNumberOfThreads(final int numberOfThreads) {
        if (numberOfThreads < 2) {
            return 2;
        }
        return numberOfThreads;
    }

    @Override
    public
    void subscribe(final Object listener) {
        if (listener == null) {
            return;
        }

//        subscriptionManager.subscribe(listener);
        subscriptionWriter.subscribe(listener);
    }

    @Override
    public
    void unsubscribe(final Object listener) {
        if (listener == null) {
            return;
        }

//        subscriptionManager.unsubscribe(listener);
        subscriptionWriter.unsubscribe(listener);
    }

    @Override
    public
    void publish(final Object message) {
        publisher.publish(syncPublication, message);
    }

    @Override
    public
    void publish(final Object message1, final Object message2) {
        publisher.publish(syncPublication, message1, message2);
    }

    @Override
    public
    void publish(final Object message1, final Object message2, final Object message3) {
        publisher.publish(syncPublication, message1, message2, message3);
    }

    @Override
    public
    void publish(final Object[] messages) {
        publisher.publish(syncPublication, messages);
    }

    @Override
    public
    void publishAsync(final Object message) {
        publisher.publish(asyncPublication, message);
    }

    @Override
    public
    void publishAsync(final Object message1, final Object message2) {
//        if (message1 != null && message2 != null) {
//            try {
//                this.dispatchQueue.transfer(message1, message2);
//            } catch (Exception e) {
//                errorHandler.handlePublicationError(new PublicationError().setMessage(
//                                "Error while adding an asynchronous message").setCause(e).setPublishedObject(message1, message2));
//            }
//        }
//        else {
//            throw new NullPointerException("Messages cannot be null.");
//        }
    }

    @Override
    public
    void publishAsync(final Object message1, final Object message2, final Object message3) {
//        if (message1 != null || message2 != null | message3 != null) {
//            try {
//                this.dispatchQueue.transfer(message1, message2, message3);
//            } catch (Exception e) {
//                errorHandler.handlePublicationError(new PublicationError().setMessage(
//                                "Error while adding an asynchronous message").setCause(e).setPublishedObject(message1, message2, message3));
//            }
//        }
//        else {
//            throw new NullPointerException("Messages cannot be null.");
//        }
    }

    @Override
    public
    void publishAsync(final Object[] messages) {
//        if (messages != null) {
//            try {
//                this.dispatchQueue.transfer(messages, MessageType.ARRAY);
//            } catch (Exception e) {
//                errorHandler.handlePublicationError(new PublicationError().setMessage(
//                                "Error while adding an asynchronous message").setCause(e).setPublishedObject(messages));
//            }
//        }
//        else {
//            throw new NullPointerException("Message cannot be null.");
//        }
    }

    @Override
    public final
    boolean hasPendingMessages() {
        return asyncPublication.hasPendingMessages();
    }

    @Override
    public final
    ErrorHandlingSupport getErrorHandler() {
        return errorHandler;
    }

    @Override
    public
    void start() {
        errorHandler.init();
        asyncPublication.start();
    }

    @Override
    public
    void shutdown() {
        this.subscriptionWriter.shutdown();
        this.asyncPublication.shutdown();
        this.subscriptionManager.shutdown();
    }
}
