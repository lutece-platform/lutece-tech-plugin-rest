<?xml version="1.0" encoding="iso-8859-1" ?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

 <xsl:include href="../../../xsl/formelements.xsl"/>

 <xsl:template match="formElements">

   <xsl:call-template name="mandatory-notice" />
   
   <xsl:apply-templates select="notices/noticeGroup[@name='transmissionGeneral']" mode="no-bullet" />

   <fieldset class="formengine-fieldset">
   <legend class="formengine-legend">[1] Transmission de vos pièces complémentaires par Internet</legend>
    
   		<xsl:apply-templates select="notices/noticeGroup[@name='transmissionInternet']" mode="no-bullet" />
   		<xsl:apply-templates select="fields/field[@name='internetComplementaryDocuments']"/>
   
    </fieldset>     
   

   <div class="formengine-steps">
            <xsl:call-template name="button-list"/>
    </div>

 </xsl:template>
</xsl:stylesheet>