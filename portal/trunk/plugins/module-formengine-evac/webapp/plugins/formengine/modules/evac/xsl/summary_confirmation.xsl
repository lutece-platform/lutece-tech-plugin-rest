<?xml version="1.0"  encoding="UTF-8"  ?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

 <xsl:template match="cartereponse">

  <fieldset class="formengine-fieldset">
      <legend class="formengine-legend">Validation</legend>
   Votre demande est en cours de transmission sous le numéro : 
    <strong><xsl:value-of  select="numero" /></strong>.

    <br/><br/>
    Vous allez recevoir un accusé réception de votre demande à l’adresse électronique que vous nous avez indiquée.
   </fieldset>


 </xsl:template>

</xsl:stylesheet>