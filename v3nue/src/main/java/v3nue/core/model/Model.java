/**
 * 
 */
package v3nue.core.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * An interface which all models/entities in application will inherit from
 * and determines themselves as Model
 * 
 * @author Ngoc Huy
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public interface Model {

}
