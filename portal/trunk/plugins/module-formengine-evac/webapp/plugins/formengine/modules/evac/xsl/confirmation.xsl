<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	
	  <xsl:include href="../../../xsl/formelements.xsl"/>
	 
	 <xsl:param name="defaultUrlToExit"/>
	 
	 	
	<xsl:template match="/">
		<form action="{$defaultUrlToExit}" id="{$formName}{$subFormName}" name="{$formName}{$subFormName}" method="post" class="formengine-form" >
			<xsl:apply-templates select="formElements"/>
		</form>
	</xsl:template>
	
    <xsl:template match="formElements">
		<fieldset class="formengine-fieldset">	
		<legend class="formengine-legend">Information</legend>	
 			<xsl:apply-templates select="notices/noticeGroup[@name='information']" mode="no-bullet"/>
		</fieldset>
		
		<xsl:apply-templates select="buttons/button[@name='Fermer']"/>
		
    </xsl:template>
    
     <xsl:template match="button[@name='Fermer']">
               <div class="formengine-steps">
               <input type="submit" value="{@name}" class="formengine-submit" />
               </div>
    </xsl:template>

</xsl:stylesheet>