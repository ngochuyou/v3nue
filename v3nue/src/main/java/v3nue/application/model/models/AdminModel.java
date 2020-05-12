/**
 * 
 */
package v3nue.application.model.models;

import v3nue.application.model.entities.Admin;
import v3nue.core.model.annotations.Relation;

/**
 * @author Ngoc Huy
 *
 */
@Relation(relation = Admin.class)
public class AdminModel extends AccountModel {

}
