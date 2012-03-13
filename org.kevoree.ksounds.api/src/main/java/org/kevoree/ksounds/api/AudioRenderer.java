package org.kevoree.ksounds.api;

/**
 * User: Erwan Daubert - erwan.daubert@gmail.com
 * Date: 13/03/12
 * Time: 20:55
 *
 * @author Erwan Daubert
 * @version 1.0
 */

import org.kevoree.annotation.*;
import org.kevoree.framework.AbstractComponentType;

@ComponentFragment
@MessageTypes({
        @MessageType(name = "format", elems = {@MsgElem(name = "bitrate", className = Float.class), @MsgElem(name = "channels", className = Integer.class), @MsgElem(name = "samplerate", className = Float.class)}),
		@MessageType(name = "format", elems = {@MsgElem(name = "data", className = byte[].class), @MsgElem(name = "length", className = Integer.class)})
})
@Provides({
		@ProvidedPort(name = "data", type = PortType.MESSAGE, messageType = "data"),
		@ProvidedPort(name = "format", type = PortType.MESSAGE, messageType = "format")
})
public abstract class AudioRenderer extends AbstractComponentType {
	
	@Port(name = "format")
	public abstract void configureFormat(Object message);

    @Port(name = "data")
	public abstract void writeData(Object message);
}
