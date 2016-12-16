/*
 Copyright Scott A. Hale, 2016
 */
package uk.ac.ox.oii.sigmaexporter.model;

public class GraphEdge extends GraphElement{
	
		
	private String label;
	private String source;
	private String target;
        private String id;
        private String type;
	
	public GraphEdge(String id) {
		super();
                this.id=id;
		label="";
		source="";
		target="";
                type="";
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

        public String getType() {
		return type;
	}

	public void setType(String type) {
            
                String edgeType = type.toLowerCase().trim().replaceAll("\\s+","");
                edgeType = (edgeType.equalsIgnoreCase("curvedarrow")) ? "curvedArrow": edgeType;
		
                this.type = edgeType;
	}
}
