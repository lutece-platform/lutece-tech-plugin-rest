<?xml version="1.0" encoding="iso-8859-1" ?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">    
    <xsl:param name="typeName"/>    
    <xsl:template match="projeau">        
        <p>Vous trouverez ci-dessous le récapitulatif complet de votre soumission de projet</p>    
        <p>Pour que votre projet soit transmis,
        veuillez confirmer les informations ci-dessous :</p>      
        <h3 class="formengine-summary-section-title">Vos coordonnées</h3>        
        <div class="formengine-element">
            <span class="formengine-summary-label">Le soumissionaire est : </span>
            <span class="formengine-summary-content">
                <b>                    
                    <xsl:value-of select="title" />
                    <xsl:text> </xsl:text>
                    <xsl:value-of select="last-name" />
                    <xsl:text> </xsl:text>
                    <xsl:value-of select="first-name" />
                    <br/>				
                    <xsl:apply-templates select="email" />                    
                </b>
            </span>
        </div>	
        
        <xsl:choose>
            <xsl:when test="complementary-documents">
                <h3 class="formengine-summary-section-title">Vos documents transmis</h3>
                <ul>
                    <xsl:apply-templates select="complementary-documents/complementary-document" />
                </ul>                
            </xsl:when>			
        </xsl:choose>
    </xsl:template>
    
    <!-- Template for the e-mail block -->
    <xsl:template match="applicant-email">
        <br/>
        <xsl:value-of select="." />
    </xsl:template>
    
    <!-- Template for the document line -->
    <xsl:template match="complementary-document">
        <li>
            <xsl:value-of select="." />
        </li>
    </xsl:template>
</xsl:stylesheet>