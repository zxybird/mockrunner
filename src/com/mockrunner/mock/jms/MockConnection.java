package com.mockrunner.mock.jms;

import javax.jms.Connection;
import javax.jms.ConnectionConsumer;
import javax.jms.ConnectionMetaData;
import javax.jms.Destination;
import javax.jms.ExceptionListener;
import javax.jms.JMSException;
import javax.jms.ServerSessionPool;
import javax.jms.Topic;

import com.mockrunner.jms.DestinationManager;

/**
 * Mock implementation of JMS <code>Connection</code>.
 */
public abstract class MockConnection implements Connection
{
    private ConnectionMetaData metaData;
    private String clientId;
    private boolean started;
    private boolean closed;
    private ExceptionListener listener;
    private JMSException exception;
    private DestinationManager destinationManager;
    
    public MockConnection(DestinationManager destinationManager)
    { 
        metaData = new MockConnectionMetaData();
        started = false;
        closed = false;
        exception = null;
        this.destinationManager = destinationManager;
    }
    
    /**
     * Returns the {@link com.mockrunner.jms.DestinationManager}.
     * @return the {@link com.mockrunner.jms.DestinationManager}
     */
    public DestinationManager getDestinationManager()
    {
        return destinationManager;
    }
    
    /**
     * Set an exception that will be thrown when calling one
     * of the interface methods. Since the mock implementation
     * cannot fail like a full blown message server you can use
     * this method to simulate server errors. After the exception
     * was thrown it will be deleted.
     * @param exception the exception to throw
     */
    public void setJMSException(JMSException exception)
    {
        this.exception = exception;
    }

    /**
     * Throws a <code>JMSException</code> if one is set with
     * {@link #setJMSException}. Informs the <code>ExceptionListener</code>
     * and deletes the exception after throwing it.
     */
    public void throwJMSException() throws JMSException
    {
        if(null == exception) return;
        JMSException tempException = exception;
        exception = null;
        if(listener != null)
        {
            listener.onException(tempException);
        }
        throw tempException;
    }
    
    /**
     * You can use this to set the <code>ConnectionMetaData</code>.
     * Usually this should not be necessary. Per default an instance
     * of {@link MockConnectionMetaData} is returned when calling
     * {@link #getMetaData}.
     * @param metaData the meta data
     */
    public void setMetaData(ConnectionMetaData metaData)
    {
        this.metaData = metaData;
    }
    
    public ConnectionConsumer createConnectionConsumer(Destination destination, String messageSelector, ServerSessionPool sessionPool, int maxMessages) throws JMSException
    {
        throwJMSException();
        return new MockConnectionConsumer(this, sessionPool);
    }

    public ConnectionConsumer createDurableConnectionConsumer(Topic topic, String subscriptionName, String messageSelector, ServerSessionPool sessionPool, int maxMessages) throws JMSException
    {
        return createConnectionConsumer(topic, messageSelector, sessionPool, maxMessages);
    }
    
    public ConnectionMetaData getMetaData() throws JMSException
    {
        throwJMSException();
        return metaData;
    }
        
    public String getClientID() throws JMSException
    {
        throwJMSException();
        return clientId;
    }

    public void setClientID(String clientId) throws JMSException
    {
        throwJMSException();
        this.clientId = clientId;
    }  

    public ExceptionListener getExceptionListener() throws JMSException
    {
        throwJMSException();
        return listener;
    }

    public void setExceptionListener(ExceptionListener listener) throws JMSException
    {
        throwJMSException();
        this.listener = listener;
    }

    public void start() throws JMSException
    {
        throwJMSException();
        started = true;
    }

    public void stop() throws JMSException
    {
        throwJMSException();
        started = false;
    }

    public void close() throws JMSException
    {
        throwJMSException();
        closed = true;
    }
    
    public boolean isStarted()
    {
        return started;
    }
    
    public boolean isStopped()
    {
        return !isStarted();
    }
    
    public boolean isClosed()
    {
        return closed;
    }
}
