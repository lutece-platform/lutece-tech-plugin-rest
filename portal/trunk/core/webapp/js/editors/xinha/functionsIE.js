InternetExplorer._pluginInfo={name:"Internet Explorer",origin:"Xinha Core",version:"$LastChangedRevision: 678 $".replace(/^[^:]*: (.*) \$$/,"$1"),developer:"The Xinha Core Developer Team",developer_url:"$HeadURL: http://svn.xinha.python-hosting.com/trunk/functionsIE.js $".replace(/^[^:]*: (.*) \$$/,"$1"),license:"htmlArea"};
function InternetExplorer(_1){
this.editor=_1;
_1.InternetExplorer=this;
}
InternetExplorer.prototype.onKeyPress=function(ev){
if(this.editor.isShortCut(ev)){
switch(this.editor.getKey(ev).toLowerCase()){
case "n":
this.editor.execCommand("formatblock",false,"<p>");
Xinha._stopEvent(ev);
return true;
break;
case "1":
case "2":
case "3":
case "4":
case "5":
case "6":
this.editor.execCommand("formatblock",false,"<h"+this.editor.getKey(ev).toLowerCase()+">");
Xinha._stopEvent(ev);
return true;
break;
}
}
switch(ev.keyCode){
case 8:
case 46:
if(this.handleBackspace()){
Xinha._stopEvent(ev);
return true;
}
break;
}
return false;
};
InternetExplorer.prototype.handleBackspace=function(){
var _3=this.editor;
var _4=_3.getSelection();
if(_4.type=="Control"){
var _5=_3.activeElement(_4);
Xinha.removeFromParent(_5);
return true;
}
var _6=_3.createRange(_4);
var r2=_6.duplicate();
r2.moveStart("character",-1);
var a=r2.parentElement();
if(a!=_6.parentElement()&&(/^a$/i.test(a.tagName))){
r2.collapse(true);
r2.moveEnd("character",1);
r2.pasteHTML("");
r2.select();
return true;
}
};
InternetExplorer.prototype.inwardHtml=function(_9){
_9=_9.replace(/<(\/?)del(\s|>|\/)/ig,"<$1strike$2");
return _9;
};
Xinha.prototype.insertNodeAtSelection=function(_a){
Xinha.notImplemented("insertNodeAtSelection");
};
Xinha.prototype.getParentElement=function(_b){
if(typeof _b=="undefined"){
_b=this.getSelection();
}
var _c=this.createRange(_b);
switch(_b.type){
case "Text":
var _d=_c.parentElement();
while(true){
var _e=_c.duplicate();
_e.moveToElementText(_d);
if(_e.inRange(_c)){
break;
}
if((_d.nodeType!=1)||(_d.tagName.toLowerCase()=="body")){
break;
}
_d=_d.parentElement;
}
return _d;
case "None":
return _c.parentElement();
case "Control":
return _c.item(0);
default:
return this._doc.body;
}
};
Xinha.prototype.activeElement=function(_f){
if((_f===null)||this.selectionEmpty(_f)){
return null;
}
if(_f.type.toLowerCase()=="control"){
return _f.createRange().item(0);
}else{
var _10=_f.createRange();
var _11=this.getParentElement(_f);
if(_11.innerHTML==_10.htmlText){
return _11;
}
return null;
}
};
Xinha.prototype.selectionEmpty=function(sel){
if(!sel){
return true;
}
return this.createRange(sel).htmlText==="";
};
Xinha.prototype.selectNodeContents=function(_13,pos){
this.focusEditor();
this.forceRedraw();
var _15;
var _16=typeof pos=="undefined"?true:false;
if(_16&&_13.tagName&&_13.tagName.toLowerCase().match(/table|img|input|select|textarea/)){
_15=this._doc.body.createControlRange();
_15.add(_13);
}else{
_15=this._doc.body.createTextRange();
_15.moveToElementText(_13);
}
_15.select();
};
Xinha.prototype.insertHTML=function(_17){
var sel=this.getSelection();
var _19=this.createRange(sel);
this.focusEditor();
_19.pasteHTML(_17);
};
Xinha.prototype.getSelectedHTML=function(){
var sel=this.getSelection();
var _1b=this.createRange(sel);
if(_1b.htmlText){
return _1b.htmlText;
}else{
if(_1b.length>=1){
return _1b.item(0).outerHTML;
}
}
return "";
};
Xinha.prototype.getSelection=function(){
return this._doc.selection;
};
Xinha.prototype.createRange=function(sel){
return sel.createRange();
};
Xinha.prototype.isKeyEvent=function(_1d){
return _1d.type=="keydown";
};
Xinha.prototype.getKey=function(_1e){
return String.fromCharCode(_1e.keyCode);
};
Xinha.getOuterHTML=function(_1f){
return _1f.outerHTML;
};
Xinha.getXMLHTTPRequestObject=function(){
return new ActiveXObject("Microsoft.XMLHTTP");
};

