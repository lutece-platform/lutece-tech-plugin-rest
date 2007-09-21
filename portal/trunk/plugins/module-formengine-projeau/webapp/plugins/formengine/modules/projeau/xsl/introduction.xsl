<?xml version="1.0" encoding="iso-8859-1" ?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

 <xsl:include href="../../../xsl/formelements.xsl"/>

 <xsl:template match="formElements">

   <xsl:call-template name="mandatory-notice" />

        
   <xsl:apply-templates select="notices/noticeGroup[@name='textIntroduction']" mode="no-bullet"/>
   
   <xsl:apply-templates select="fields/field[@name='confirmRead']"/>

   

   <div class="formengine-steps">
            <xsl:call-template name="button-list"/>
    </div>

 </xsl:template>



<!-- checkbox -->

<xsl:template match="field[@name='confirmRead']" >


	<div class="formengine-element">
				<span class="formengine-label">
					<label for="{@name}">
						<xsl:if test="boolean(checkFieldRules/checkRule[@type='fieldRequired'])">
							<span class="formengine-mandatory" > * </span>
						</xsl:if>
						<xsl:value-of select="label"/>
						<xsl:text> :</xsl:text>
					</label>
						<xsl:apply-templates select="additionalInfo"/>


				</span>
				<span class="formengine-field" >
					<input type="checkbox" name="{@name}" id="{@name}" value="1"/>
				</span>
	</div>


</xsl:template>


 
 </xsl:stylesheet>