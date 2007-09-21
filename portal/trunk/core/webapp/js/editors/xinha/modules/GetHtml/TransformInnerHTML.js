/**
  * Based on XML_Utility functions submitted by troels_kn.
  * credit also to adios, who helped with reg exps:
  * http://www.sitepoint.com/forums/showthread.php?t=201052
  * 
  * A replacement for Xinha.getHTML
  *
  * Features:
  *   - Generates XHTML code
  *   - Much faster than Xinha.getHTML
  *   - Eliminates the hacks to accomodate browser quirks
  *   - Returns correct code for Flash objects and scripts
  *   - Formats html in an indented, readable format in html mode
  *   - Preserves script and pre formatting
  *   - Preserves formatting in comments
  *   - Removes contenteditable from body tag in full-page mode
  *   - Supports only7BitPrintablesInURLs config option
  *   - Supports htmlRemoveTags config option
  */
  
function GetHtmlImplementation(editor) {
    this.editor = editor;
}

GetHtmlImplementation._pluginInfo = {
	name          : "GetHtmlImplementation TransformInnerHTML",
	version       : "1.0",
	developer     : "Nelson Bright",
	developer_url : "http://www.brightworkweb.com/",
	sponsor       : "",
    sponsor_url   : "",
	license       : "htmlArea"
};

Xinha.RegExpCache = [
/*00*/  new RegExp().compile(/<\s*\/?([^\s\/>]+)[\s*\/>]/gi),//lowercase tags
/*01*/  new RegExp().compile(/(\s+)_moz[^=>]*=[^\s>]*/gi),//strip _moz attributes
/*02*/  new RegExp().compile(/\s*=\s*(([^'"][^>\s]*)([>\s])|"([^"]+)"|'([^']+)')/g),// find attributes
/*03*/  new RegExp().compile(/\/>/g),//strip singlet terminators
/*04*/  new RegExp().compile(/<(br|hr|img|input|link|meta|param|embed|area)((\s*\S*="[^"]*")*)>/g),//terminate singlet tags
/*05*/  new RegExp().compile(/(<\w+\s+(\w*="[^"]*"\s+)*)(checked|compact|declare|defer|disabled|ismap|multiple|no(href|resize|shade|wrap)|readonly|selected)([\s>])/gi),//expand singlet attributes
/*06*/  new RegExp().compile(/(="[^']*)'([^'"]*")/),//check quote nesting
/*07*/  new RegExp().compile(/&(?=[^<]*>)/g),//expand query ampersands
/*08*/  new RegExp().compile(/<\s+/g),//strip tagstart whitespace
/*09*/  new RegExp().compile(/\s+(\/)?>/g),//trim whitespace
/*10*/  new RegExp().compile(/\s{2,}/g),//trim extra whitespace
/*11*/  new RegExp().compile(/\s+([^=\s]+)((="[^"]+")|([\s>]))/g),// lowercase attribute names
/*12*/  new RegExp().compile(/\s+contenteditable(=[^>\s\/]*)?/gi),//strip contenteditable
/*13*/  new RegExp().compile(/((href|src)=")([^\s]*)"/g), //find href and src for stripBaseHref()
/*14*/  new RegExp().compile(/<\/?(div|p|h[1-6]|table|tr|td|th|ul|ol|li|blockquote|object|br|hr|img|embed|param|pre|script|html|head|body|meta|link|title|area|input|form|textarea|select|option)[^>]*>/g),
/*15*/  new RegExp().compile(/<\/(div|p|h[1-6]|table|tr|ul|ol|blockquote|object|html|head|body|script|form|select)( [^>]*)?>/g),//blocklevel closing tag
/*16*/  new RegExp().compile(/<(div|p|h[1-6]|table|tr|ul|ol|blockquote|object|html|head|body|script|form|select)( [^>]*)?>/g),//blocklevel opening tag
/*17*/  new RegExp().compile(/<(td|th|li|option|br|hr|embed|param|pre|meta|link|title|area|input|textarea)[^>]*>/g),//singlet tag or output on 1 line
/*18*/  new RegExp().compile(/(^|<\/(pre|script)>)(\s|[^\s])*?(<(pre|script)[^>]*>|$)/g),//find content NOT inside pre and script tags
/*19*/  new RegExp().compile(/(<pre[^>]*>)([\s\S])*?(<\/pre>)/g),//find content inside pre tags
/*20*/  new RegExp().compile(/(^|<!--[\s\S]*?-->)([\s\S]*?)(?=<!--[\s\S]*?-->|$)/g),//find content NOT inside comments
/*21*/  new RegExp().compile(/\S*=""/g), //find empty attributes
/*22*/  new RegExp().compile(/<!--[\s\S]*?-->|<\?[\s\S]*?\?>|<\/?\w[^>]*>/g), //find all tags, including comments and php
/*23*/  new RegExp().compile(/(^|<\/script>)[\s\S]*?(<script[^>]*>|$)/g) //find content NOT inside script tags
];

/** 
  * Cleans HTML into wellformed xhtml
  */
Xinha.prototype.cleanHTML = function(sHtml) {
	var c = Xinha.RegExpCache;
	sHtml = sHtml.
		replace(c[0], function(str) { return str.toLowerCase(); } ).//lowercase tags/attribute names
		replace(c[1], ' ').//strip _moz attributes
		replace(c[12], ' ').//strip contenteditable
		replace(c[2], '="$2$4$5"$3').//add attribute quotes
		replace(c[21], ' ').//strip empty attributes
		replace(c[11], function(str, p1, p2) { return ' '+p1.toLowerCase()+p2; }).//lowercase attribute names
		replace(c[3], '>').//strip singlet terminators
		replace(c[9], '$1>').//trim whitespace
		replace(c[5], '$1$3="$3"$4').//expand singlet attributes
		replace(c[4], '<$1$2 />').//terminate singlet tags
		replace(c[6], '$1$2').//check quote nesting
	//	replace(c[7], '&amp;').//expand query ampersands
		replace(c[8], '<').//strip tagstart whitespace
		replace(c[10], ' ');//trim extra whitespace
	if(Xinha.is_ie && c[13].test(sHtml)) {
		sHtml = sHtml.replace(c[13],'$1'+this.stripBaseURL(RegExp.$3)+'"');
	}
	if(this.config.only7BitPrintablesInURLs) {
		if (Xinha.is_ie) c[13].test(sHtml); // oddly the test below only triggers when we call this once before (IE6), in Moz it fails if tested twice
		if ( c[13].test(sHtml)) {
			try { //Mozilla returns an incorrectly encoded value with innerHTML
				sHtml = sHtml.replace(c[13], '$1'+decodeURIComponent(RegExp.$3).replace(/([^!-~]+)/g,function(chr){return escape(chr);})+'"');
			} catch (e) { // once the URL is escape()ed, you can't decodeURIComponent() it anymore
				sHtml = sHtml.replace(c[13], '$1'+RegExp.$3.replace(/([^!-~]+)/g,function(chr){return escape(chr);})+'"');
			}
		}
	}
	return sHtml;
};

/**
  * Prettyfies html by inserting linebreaks before tags, and indenting blocklevel tags
  */
Xinha.indent = function(s, sindentChar) {
	Xinha.__nindent = 0;
	Xinha.__sindent = "";
	Xinha.__sindentChar = (typeof sindentChar == "undefined") ? "  " : sindentChar;
	var c = Xinha.RegExpCache;
	if(Xinha.is_gecko) { //moz changes returns into <br> inside <pre> tags
		s = s.replace(c[19], function(str){return str.replace(/<br \/>/g,"\n")});
	}
	s = s.replace(c[18], function(strn) { //skip pre and script tags
	  strn = strn.replace(c[20], function(st,$1,$2) { //exclude comments
		string = $2.replace(/[\n\r]/gi, " ").replace(/\s+/gi," ").replace(c[14], function(str) {
			if (str.match(c[16])) {
				var s = "\n" + Xinha.__sindent + str;
				// blocklevel openingtag - increase indent
				Xinha.__sindent += Xinha.__sindentChar;
				++Xinha.__nindent;
				return s;
			} else if (str.match(c[15])) {
				// blocklevel closingtag - decrease indent
				--Xinha.__nindent;
				Xinha.__sindent = "";
				for (var i=Xinha.__nindent;i>0;--i) {
					Xinha.__sindent += Xinha.__sindentChar;
				}
				return "\n" + Xinha.__sindent + str;
			} else if (str.match(c[17])) {
				// singlet tag
				return "\n" + Xinha.__sindent + str;
			}
			return str; // this won't actually happen
		});
		return $1 + string;
	  });return strn;
    });
    //final cleanup
    s = s.replace(/^\s*/,'').//strip leading whitespace
        replace(/ +\n/g,'\n').//strip spaces at end of lines
        replace(/[\r\n]+<\/script>/g,'\n</script>');//strip returns added into scripts
    return s;
};

Xinha.getHTML = function(root, outputRoot, editor) {
	var html = "";
	var c = Xinha.RegExpCache;

	if(root.nodeType == 11) {//document fragment
	    //we can't get innerHTML from the root (type 11) node, so we 
	    //copy all the child nodes into a new div and get innerHTML from the div
	    var div = document.createElement("div");
	    var temp = root.insertBefore(div,root.firstChild);
	    for (j = temp.nextSibling; j; j = j.nextSibling) { 
	    		temp.appendChild(j.cloneNode(true));
	    }
		html += temp.innerHTML.replace(c[23], function(strn) { //skip content inside script tags
			strn = strn.replace(c[22], function(tag){
				if(/^<[!\?]/.test(tag)) return tag; //skip comments and php tags
				else return editor.cleanHTML(tag)});
			return strn;
		});

	} else {

		var root_tag = (root.nodeType == 1) ? root.tagName.toLowerCase() : ''; 
		if (outputRoot) { //only happens with <html> tag in fullpage mode
			html += "<" + root_tag;
			var attrs = root.attributes; // strangely, this doesn't work in moz
			for (i = 0; i < attrs.length; ++i) {
				var a = attrs.item(i);
				if (!a.specified) {
				  continue;
				}
				var name = a.nodeName.toLowerCase();
				var value = a.nodeValue;
				html += " " + name + '="' + value + '"';
			}
			html += ">";
		}
		if(root_tag == "html") {
			innerhtml = editor._doc.documentElement.innerHTML;
		} else {
			innerhtml = root.innerHTML;
		}
		//pass tags to cleanHTML() one at a time
		//includes support for htmlRemoveTags config option
		html += innerhtml.replace(c[23], function(strn) { //skip content inside script tags
			strn = strn.replace(c[22], function(tag){
				if(/^<[!\?]/.test(tag)) return tag; //skip comments and php tags
				else if(!(editor.config.htmlRemoveTags && editor.config.htmlRemoveTags.test(tag.replace(/<([^\s>\/]+)/,'$1'))))
					return editor.cleanHTML(tag);
				else return ''});
			return strn;
		});
		//IE drops  all </li> tags in a list except the last one
		if(Xinha.is_ie) {
			html = html.replace(/<li( [^>]*)?>/g,'</li><li$1>').
				replace(/(<(ul|ol)[^>]*>)[\s\n]*<\/li>/g, '$1').
				replace(/<\/li>([\s\n]*<\/li>)+/g, '<\/li>');
		}
		if(Xinha.is_gecko)
			html = html.replace(/<br \/>\n$/, ''); //strip trailing <br> added by moz
		if (outputRoot) {
			html += "</" + root_tag + ">";
		}
		html = Xinha.indent(html);
	};
//	html = Xinha.htmlEncode(html);

	return html;
};
