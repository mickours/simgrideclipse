//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.05.29 at 03:18:10 PM CEST 
//


package simgrideclipseplugin.model.simgrid;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;choice maxOccurs="unbounded" minOccurs="0">
 *         &lt;element ref="{}include"/>
 *         &lt;element ref="{}cluster"/>
 *         &lt;element ref="{}peer"/>
 *         &lt;element ref="{}AS"/>
 *         &lt;element ref="{}trace"/>
 *         &lt;element ref="{}trace_connect"/>
 *       &lt;/choice>
 *       &lt;attribute name="file" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "includeOrClusterOrPeer"
})
@XmlRootElement(name = "include")
public class Include {

    @XmlElements({
        @XmlElement(name = "cluster", type = Cluster.class),
        @XmlElement(name = "AS", type = AS.class),
        @XmlElement(name = "trace_connect", type = TraceConnect.class),
        @XmlElement(name = "trace", type = Trace.class),
        @XmlElement(name = "peer", type = Peer.class),
        @XmlElement(name = "include", type = Include.class)
    })
    protected List<Object> includeOrClusterOrPeer;
    @XmlAttribute(required = true)
    @XmlSchemaType(name = "anySimpleType")
    protected String file;

    /**
     * Gets the value of the includeOrClusterOrPeer property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the includeOrClusterOrPeer property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getIncludeOrClusterOrPeer().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Cluster }
     * {@link AS }
     * {@link TraceConnect }
     * {@link Trace }
     * {@link Peer }
     * {@link Include }
     * 
     * 
     */
    public List<Object> getIncludeOrClusterOrPeer() {
        if (includeOrClusterOrPeer == null) {
            includeOrClusterOrPeer = new ArrayList<Object>();
        }
        return this.includeOrClusterOrPeer;
    }

    /**
     * Gets the value of the file property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFile() {
        return file;
    }

    /**
     * Sets the value of the file property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFile(String value) {
        this.file = value;
    }

}
