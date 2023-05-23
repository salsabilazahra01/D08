package propensi.project.water.setting.xml;

import lombok.Getter;
import lombok.Setter;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@Setter
@Getter
@XmlAccessorType(XmlAccessType.FIELD)
public class Attributes {
    @XmlElement(namespace = "http://www.yale.edu/tp/cas")
    private String ldap_cn;

    @XmlElement(namespace = "http://www.yale.edu/tp/cas")
    private String kd_org;

    @XmlElement(namespace = "http://www.yale.edu/tp/cas")
    private String peran_user;

    @XmlElement(namespace = "http://www.yale.edu/tp/cas")
    private String nama;

    @XmlElement(namespace = "http://www.yale.edu/tp/cas")
    private String npm;
}
