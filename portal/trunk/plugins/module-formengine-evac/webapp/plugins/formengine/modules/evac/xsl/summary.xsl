<?xml version="1.0"  encoding="UTF-8"  ?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

 <xsl:include href="../../../xsl/formelements.xsl"/>
 
 <xsl:param name="hideButtonSecondFamily"/>
 	
 <xsl:template match="formElements">
	
	<xsl:if test="$hideButtonSecondFamily=0">
	<fieldset class="formengine-fieldset">
	<legend class="formengine-legend">2de famille</legend>		
 		<xsl:apply-templates select="notices/noticeGroup[@name='Seconde famille']"/>
		<div class="formengine-steps">
 			<xsl:apply-templates select="buttons/button[@name='2de famille']"/>
 		</div>
	</fieldset>
	</xsl:if>
	<fieldset class="formengine-fieldset">
	<legend class="formengine-legend">Information</legend>		
 			<xsl:apply-templates select="notices/noticeGroup[@name='Remarque']" mode="no-bullet"/>
	</fieldset>
 <div class="formengine-steps">
 		
      	<xsl:apply-templates select="buttons/button[@name='Valider']"/>
 </div>

 </xsl:template>
</xsl:stylesheet>