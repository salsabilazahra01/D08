package propensi.project.water.setting.xml;

import lombok.Getter;
import lombok.Setter;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@Setter
@Getter
@XmlAccessorType(XmlAccessType.FIELD)
public class AuthenticationSuccess {
    @XmlElement(name = "user", namespace = "http://www.yale.edu/tp/cas")
    private String user;

    @XmlElement(name = "attributes", namespace = "http://www.yale.edu/tp/cas")
    private Attributes attributes;
}
