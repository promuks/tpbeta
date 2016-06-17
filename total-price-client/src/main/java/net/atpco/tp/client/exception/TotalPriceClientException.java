package net.atpco.tp.client.exception;


public class TotalPriceClientException  extends Exception {

	private static final long serialVersionUID = 6703347735514863123L;
	private String queryId;
	private String origin;
	private String destination;
	private String carrier;
	private String fareClass;
	
	
	public TotalPriceClientException(final Throwable cause){
		super(cause);
	}
	public TotalPriceClientException(final String msg, final Throwable cause){
		super(msg,cause);
	}
	
	public TotalPriceClientException(final String queryId, final String origin,
			final String destination, 
			final String carrier, final String fareClass, final Throwable cause) {
		
		super(cause);		
		this.queryId = queryId;
		this.origin = origin;
		this.destination = destination;
		this.fareClass = fareClass;		
		this.carrier = carrier;
	}
	
	public String getQueryId() {
		return queryId;
	}
	public void setQueryId(final String queryId) {
		this.queryId = queryId;
	}
	public String getOrigin() {
		return origin;
	}
	public void setOrigin(final String origin) {
		this.origin = origin;
	}
	public String getDestination() {
		return destination;
	}
	public void setDestination(final String destination) {
		this.destination = destination;
	}
	public String getFareClass() {
		return fareClass;
	}
	public void setFareClass(final String fareClass) {
		this.fareClass = fareClass;
	}
	
	public String getCarrier() {
		return carrier;
	}
	public void setCarrier(final String carrier) {
		this.carrier = carrier;
	}
	@Override
	public String toString() {
		return "TotalPriceClientException [queryId=" + queryId
				+ ", origin=" + origin + ", destination=" + destination
				+ ", fareClass=" + fareClass + ", carrier=" + carrier + "]";
	}
	
}
