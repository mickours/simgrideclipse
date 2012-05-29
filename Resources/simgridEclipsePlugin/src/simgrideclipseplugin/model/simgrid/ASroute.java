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
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{}link_ctn" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="src" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *       &lt;attribute name="dst" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *       &lt;attribute name="gw_src" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *       &lt;attribute name="gw_dst" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *       &lt;attribute name="symmetrical" default="YES">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}token">
 *             &lt;enumeration value="YES"/>
 *             &lt;enumeration value="NO"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "linkCtn"
})
@XmlRootElement(name = "ASroute")
public class ASroute {

    @XmlElement(name = "link_ctn")
    protected List<LinkCtn> linkCtn;
    @XmlAttribute(required = true)
    @XmlSchemaType(name = "anySimpleType")
    protected String src;
    @XmlAttribute(required = true)
    @XmlSchemaType(name = "anySimpleType")
    protected String dst;
    @XmlAttribute(name = "gw_src", required = true)
    @XmlSchemaType(name = "anySimpleType")
    protected String gwSrc;
    @XmlAttribute(name = "gw_dst", required = true)
    @XmlSchemaType(name = "anySimpleType")
    protected String gwDst;
    @XmlAttribute
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String symmetrical;

    /**
     * Gets the value of the linkCtn property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the linkCtn property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getLinkCtn().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link LinkCtn }
     * 
     * 
     */
    public List<LinkCtn> getLinkCtn() {
        if (linkCtn == null) {
            linkCtn = new ArrayList<LinkCtn>();
        }
        return this.linkCtn;
    }

    /**
     * Gets the value of the src property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSrc() {
        return src;
    }

    /**
     * Sets the value of the src property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSrc(String value) {
        this.src = value;
    }

    /**
     * Gets the value of the dst property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDst() {
        return dst;
    }

    /**
     * Sets the value of the dst property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDst(String value) {
        this.dst = value;
    }

    /**
     * Gets the value of the gwSrc property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGwSrc() {
        return gwSrc;
    }

    /**
     * Sets the value of the gwSrc property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGwSrc(String value) {
        this.gwSrc = value;
    }

    /**
     * Gets the value of the gwDst property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGwDst() {
        return gwDst;
    }

    /**
     * Sets the value of the gwDst property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGwDst(String value) {
        this.gwDst = value;
    }

    /**
     * Gets the value of the symmetrical property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSymmetrical() {
        if (symmetrical == null) {
            return "YES";
        } else {
            return symmetrical;
        }
    }

    /**
     * Sets the value of the symmetrical property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSymmetrical(String value) {
        this.symmetrical = value;
    }

}
