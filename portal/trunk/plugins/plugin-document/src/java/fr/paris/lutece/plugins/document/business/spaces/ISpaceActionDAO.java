/*
 * ISpaceActionDAO.java
 *
 * Created on 29 septembre 2006, 14:13
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package fr.paris.lutece.plugins.document.business.spaces;

import java.util.List;


/**
 *
 * @author LEVY
 */
public interface ISpaceActionDAO
{
    /**
     * Load the list of actions for a document
     *
     * @return The Collection of actions
     */
    List<SpaceAction> selectActions(  );
}
