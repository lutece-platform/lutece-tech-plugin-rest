<?xml version="1.0" encoding="iso-8859-1" ?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

 <xsl:param name="defaultUrlToExit"/>
 <xsl:param name="form"/>
 <xsl:param name="transactionId"/>
 <xsl:param name="lastName" />
 <xsl:param name="firstName" />
 <xsl:param name="title" />
 <xsl:param name="email" />
  <xsl:param name="documentNamesList" />
 
  <xsl:param name="mediationRequest"/>
  

    <xsl:template match="formElements">
    	 
    	 <xsl:apply-templates select="notices/noticeGroup[@name='id']" mode="no-bullet"/>
    	 	
    	 <xsl:apply-templates select="notices/noticeGroup[@name='notices']" mode="no-bullet"/>
    

	<div class="formengine-steps">
        	<xsl:apply-templates select="buttons/button[@name='Imprimer votre dossier']"/>
        	<xsl:apply-templates select="buttons/button[@name='Terminer']"/>
         </div>

    </xsl:template>
    
    
    <!-- only one notice should be in that group.
    The transaction id is added at the end -->
    <xsl:template match="noticeGroup[@name='id']" mode="no-bullet" >
            <p>
                <xsl:value-of select="notice" disable-output-escaping="yes" />
                <xsl:text> </xsl:text>
                <b><xsl:text>[</xsl:text><xsl:value-of select="$transactionId" /><xsl:text>]</xsl:text></b>
                <xsl:text>.</xsl:text>
           </p>
    </xsl:template>
    

    <!-- button to exit the form -->
    <xsl:template match="button[@name='Terminer']">
           <form action="{$defaultUrlToExit}" id="form-end" >
               <input type="hidden" name="page" value="formengine" />
               <input type="hidden" name="form" value="{$form}" />
               <input type="submit" value="{@name}" class="formengine-submit" />
           </form>
    </xsl:template>   
    
    <!-- templates to display the notices in paragraphs, instead of lists with bullets  -->
    	<xsl:template match="noticeGroup" mode="no-bullet" >
            <p>
                <xsl:apply-templates select="notice" mode="no-bullet" />
           </p>
        </xsl:template>
        <xsl:template match="notice" mode="no-bullet">
            <p>
                <xsl:value-of select="." disable-output-escaping="yes" />
            </p>
    </xsl:template>
    
    
</xsl:stylesheet>
