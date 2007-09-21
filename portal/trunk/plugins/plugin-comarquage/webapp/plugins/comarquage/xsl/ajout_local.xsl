<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet 
	version="1.0" 
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"

	xmlns:axslt="http://xml.apache.org/xslt"
	xmlns:xalan="http://xml.apache.org/xalan"
	xmlns:comarquage="fr.paris.lutece.plugins.comarquage.util.localnodes"
	
>
<!-- 
<xalan:component prefix="comarquage"
                 functions="nodesAttached,resolvePath">
	<xalan:script lang="javaclass"
	              src="fr.paris.lutece.plugins.comarquage.util.localnodes.XslXalanExtension"/>
</xalan:component>
-->

<xsl:param name="pluginName"></xsl:param>
<xsl:param name="initialKey"></xsl:param>

<xsl:variable name="newChilds"  select="xalan://comarquage:nodesAttached(substring-before(/fiche_localisee/fiche_pratique/@url, '.xml'),$pluginName)"/>

<xsl:template match="@*|node()">
  <xsl:copy>
    <xsl:apply-templates select="@*|node()"/>
  </xsl:copy>
</xsl:template>

<xsl:template match="noeuds_fils">
  <xsl:copy>
      
       <xsl:for-each select="*">
       	    <xsl:apply-templates select="."/>
       </xsl:for-each>
 
	   <xsl:value-of disable-output-escaping="yes" select="$newChilds"/>
 
  </xsl:copy>
</xsl:template>



<xsl:template match="infos_techniques">
    
    <xsl:variable name="newPath"  select="xalan://comarquage:resolvePath($initialKey,.,$pluginName)"/>
   
    <xsl:choose>
        <xsl:when test="$newPath != ''">
            <!-- if the new path calculated is not empty, print it -->
            <xsl:value-of disable-output-escaping="yes" select="$newPath"/>
            
        </xsl:when>
        <xsl:otherwise>
            <xsl:variable name="default-id" select="substring-before(/fiche_localisee/fiche_pratique/@url,'.')"/>
            <xsl:variable name="default-path" select="concat('N0/',$default-id)"/>
            <xsl:variable name="default-title" select="/fiche_localisee/fiche_pratique/titre"/>
            
            <infos_techniques>
                   <chemin id="{$default-path}" >
                       <fiche_tech id="N0" path="N0" />
                       <fiche_tech id="{$default-id}" path="{$default-path}" titre="{$default-title}" />           
                   </chemin>
             </infos_techniques>
              
        </xsl:otherwise>
    </xsl:choose>
	
</xsl:template>


</xsl:stylesheet>


