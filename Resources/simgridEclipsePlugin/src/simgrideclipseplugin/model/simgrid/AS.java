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
 *         &lt;element ref="{}AS"/>
 *         &lt;element ref="{}cluster"/>
 *         &lt;element ref="{}peer"/>
 *         &lt;element ref="{}ASroute"/>
 *         &lt;element ref="{}bypassASroute"/>
 *         &lt;element ref="{}host"/>
 *         &lt;element ref="{}route"/>
 *         &lt;element ref="{}bypassRoute"/>
 *         &lt;element ref="{}include"/>
 *         &lt;element ref="{}router"/>
 *         &lt;element ref="{}link"/>
 *         &lt;element ref="{}storage_type"/>
 *         &lt;element ref="{}storage"/>
 *         &lt;element ref="{}trace"/>
 *         &lt;element ref="{}trace_connect"/>
 *       &lt;/choice>
 *       &lt;attribute name="id" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *       &lt;attribute name="routing" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "asOrClusterOrPeer"
})
@XmlRootElement(name = "AS")
public class AS {

    @XmlElements({
        @XmlElement(name = "trace_connect", type = TraceConnect.class),
        @XmlElement(name = "AS", type = AS.class),
        @XmlElement(name = "cluster", type = Cluster.class),
        @XmlElement(name = "bypassASroute", type = BypassASroute.class),
        @XmlElement(name = "bypassRoute", type = BypassRoute.class),
        @XmlElement(name = "peer", type = Peer.class),
        @XmlElement(name = "host", type = Host.class),
        @XmlElement(name = "include", type = Include.class),
        @XmlElement(name = "router", type = Router.class),
        @XmlElement(name = "trace", type = Trace.class),
        @XmlElement(name = "link", type = Link.class),
        @XmlElement(name = "storage_type", type = StorageType.class),
        @XmlElement(name = "storage", type = Storage.class),
        @XmlElement(name = "ASroute", type = ASroute.class),
        @XmlElement(name = "route", type = Route.class)
    })
    protected List<Object> asOrClusterOrPeer;
    @XmlAttribute(required = true)
    @XmlSchemaType(name = "anySimpleType")
    protected String id;
    @XmlAttribute(required = true)
    @XmlSchemaType(name = "anySimpleType")
    protected String routing;

    /**
     * Gets the value of the asOrClusterOrPeer property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the asOrClusterOrPeer property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getASOrClusterOrPeer().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TraceConnect }
     * {@link AS }
     * {@link Cluster }
     * {@link BypassASroute }
     * {@link BypassRoute }
     * {@link Peer }
     * {@link Host }
     * {@link Include }
     * {@link Router }
     * {@link Trace }
     * {@link Link }
     * {@link StorageType }
     * {@link Storage }
     * {@link ASroute }
     * {@link Route }
     * 
     * 
     */
    public List<Object> getASOrClusterOrPeer() {
        if (asOrClusterOrPeer == null) {
            asOrClusterOrPeer = new ArrayList<Object>();
        }
        return this.asOrClusterOrPeer;
    }

    /**
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setId(String value) {
        this.id = value;
    }

    /**
     * Gets the value of the routing property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRouting() {
        return routing;
    }

    /**
     * Sets the value of the routing property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRouting(String value) {
        this.routing = value;
    }

}
