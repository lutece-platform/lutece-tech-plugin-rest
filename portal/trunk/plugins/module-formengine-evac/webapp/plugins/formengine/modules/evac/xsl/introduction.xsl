<?xml version="1.0"  ?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
 
	<xsl:include href="../../../xsl/formelements.xsl"/>

	<xsl:template match="formElements">
		
       	<fieldset class="formengine-fieldset">
       	<legend class="formengine-legend">Règlement</legend>
   			<xsl:apply-templates select="notices/noticeGroup[@name='Reglement']"/>
   			<xsl:apply-templates select="notices/noticeGroup[@name='Reglement2']"/>
   			<xsl:apply-templates select="notices/noticeGroup[@name='Reglement3']"/>
   			<xsl:apply-templates select="notices/noticeGroup[@name='Date ouverture']"/>
   			<xsl:apply-templates select="notices/noticeGroup[@name='Reglement4']"/>
   			<xsl:apply-templates select="notices/noticeGroup[@name='Reglement5']"/>
   			<xsl:apply-templates select="notices/noticeGroup[@name='Reglement6']"/>
   			<xsl:apply-templates select="notices/noticeGroup[@name='Reglement7']"/>
   			<xsl:apply-templates select="notices/noticeGroup[@name='Plus information']"/>
   		</fieldset>
   		
   		<xsl:call-template name="mandatory-notice" />
   		
   		<fieldset class="formengine-fieldset">
   			<legend class="formengine-legend">Accès à la demande de rendez-vous en ligne</legend>
   			<xsl:apply-templates select="notices/noticeGroup[@name='Conditions générales']" mode="no-bullet"/>
	     	<xsl:apply-templates select="fields/field[@name='confirmRead']"/>

			<xsl:call-template name="captcha" />
		</fieldset>

     	<div class="formengine-steps">
          <xsl:apply-templates select="buttons/button[@name='Débuter votre démarche']"/>
     	</div>
	
	</xsl:template>

</xsl:stylesheet>