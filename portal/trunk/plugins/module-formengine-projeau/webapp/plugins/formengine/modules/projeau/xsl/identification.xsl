<?xml version="1.0" encoding="iso-8859-1" ?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

 <xsl:include href="../../../xsl/formelements.xsl"/>

 <xsl:template match="formElements">

   <xsl:call-template name="mandatory-notice" />

  
  
  <fieldset class="formengine-fieldset">
   <legend class="formengine-legend">Votre identité et vos coordonnées</legend>
    
   		<b>
   		<xsl:apply-templates select="notices/noticeGroup[@name='warning']" mode="no-bullet"/>
   		</b>
    
        
   		<xsl:apply-templates select="fields/field[@name='Title']"/>
   		<xsl:apply-templates select="fields/field[@name='LastName']"/>
   		<xsl:apply-templates select="fields/field[@name='FirstName']"/>
   		
   		<xsl:apply-templates select="fields/field[@name='email']"/>
   		

    </fieldset>

   <div class="formengine-steps">
            <xsl:call-template name="button-list"/>
    </div>
    
    <p><xsl:apply-templates select="notices/noticeGroup[@name='privacy']" mode="no-bullet"/></p>

 </xsl:template>
 
 
 
</xsl:stylesheet>