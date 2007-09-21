<?xml version="1.0"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">


<xsl:template match="portlet">
	<div class="portlet">
        <xsl:if test="not(string(display-portlet-title)='1')">
			<h3 class="portlet-header">
				<xsl:value-of disable-output-escaping="yes" select="portlet-name" />
			</h3>
        </xsl:if>

		<div class="portlet-content">
	    	<xsl:apply-templates select="mylutece-portlet" />
		</div>
	</div>
</xsl:template>


<xsl:template match="mylutece-portlet">
	<xsl:apply-templates select="user-not-signed" />
	<xsl:apply-templates select="lutece-user" />
</xsl:template>


<xsl:template match="user-not-signed">

		<form name="login" action="jsp/site/plugins/mylutece/DoMyLuteceLogin.jsp" method="post">		  <center> 
		   <p class="login">
                    <label for="username">Code d'accès :</label>
                    <input name="username" id="username" autocomplete="off" tabindex="1" type="text"/>
                </p>
		<p class="login">

                    <label for="password">Mot de passe :</label>
		   			 <input name="password" id="password" autocomplete="off" tabindex="2" type="password" />
                </p>
		          
                <p>
                    <input class="button" value="Connexion" tabindex="3" type="submit" />
                </p>
		   
		   </center>
	   </form>

		   		<xsl:apply-templates select="lutece-user-new-account-url" />

   				<xsl:apply-templates select="lutece-user-lost-password-url" />

</xsl:template>


<xsl:template match="lutece-user">
    Bienvenue   <xsl:value-of disable-output-escaping="yes" select="lutece-user-name-given" />&#160;
                <xsl:value-of disable-output-escaping="yes" select="lutece-user-name-family" />
    <br />
    <br />
    <xsl:apply-templates select="lutece-user-logout-url" />
    <xsl:apply-templates select="lutece-user-view-account-url" />
</xsl:template>


<xsl:template match="lutece-user-logout-url">
   <form name="logout" action="{.}" method="post">
   	<center>
	   	<input type="submit" value="D&#233;connexion"/>
    </center>
   </form>
</xsl:template>


<xsl:template match="lutece-user-new-account-url">

	<div id="lutece-user-new-account" >
	<form name="logout" action="{.}" method="post">
    	<center>
		   	<input type="submit" value="Cr&#233;er un compte"/>
	    </center>
    </form>
    </div>
</xsl:template>


<xsl:template match="lutece-user-lost-password-url">
	<div id="lutece-user-lost-password" >
	<form name="logout" action="{.}" method="post">
    	<center>
		   	<input type="submit" value="Mot de passe perdu"/>
	    </center>
   </form>
   </div>
</xsl:template>

<xsl:template match="lutece-user-view-account-url">
	<form name="logout" action="{.}" method="post">
    	<center>
		   	<input type="submit" value="Voir mon compte"/>
	    </center>
    </form>
</xsl:template>

</xsl:stylesheet>

