/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

function generateCode(dialogID)
{
    var a = Math.ceil(Math.random() * 9) + '';
    var b = Math.ceil(Math.random() * 9) + '';
    var c = Math.ceil(Math.random() * 9) + '';
    var d = Math.ceil(Math.random() * 9) + '';
    var e = Math.ceil(Math.random() * 9) + '';

    var code = a + b + c + d + e;

    document.getElementById(dialogID+'Input').checked=false;
    document.getElementById(dialogID+'Message').innerHTML ="";  
}

function checkCode(dialogID,alert1,alert2) {
 
    var why = "";
    document.getElementById(dialogID+'Input').focus(); 
    var input = document.getElementById(dialogID+'Input');

    why=alert1;
    if (!input.checked) {
        document.getElementById(dialogID+'Message').innerHTML =  " <div class='ui-messages-error ui-corner-all'><span class='ui-messages-error-icon'></span><span class='ui-messages-error-summary'>"+why+"</span></div>";
        return false;
    }
    else
    {
        return true;
    }
}

function ValidCaptcha(dialogID){
//    var input = document.getElementById(dialogID+'Input').value;
//    var code = document.getElementById(dialogID+'Code').value;
//	var str1 = removeSpaces(input);
//	var str2 = removeSpaces(code);
//	if (str1 == str2){
//		return true;	
//	}else{
//		return false;
//	}
}

// Remove the spaces from the entered and generated code
function removeSpaces(string){
	return string.split(' ').join('');
}


function enterEvent(t) {
    if (event.keyCode === 13 || event.keyCode === 108) {
        var id = t.id;
        var pid = document.getElementById(id).parentNode.parentNode.parentNode.parentNode.parentNode.parentNode.id;
        var okid = pid.substr(0,pid.length-10) + "okButton";
        document.getElementById(okid).focus();
    }
}
function ini_message(dialogID)
{
   document.getElementById(dialogID+'Message').innerHTML =""; 
}
function check(dialogID,why,flag)
{
    if(flag==="true")
    {

        document.getElementById(dialogID+'Message').innerHTML =  " <div class='ui-messages-error ui-corner-all'><span class='ui-messages-error-icon'></span><span class='ui-messages-error-summary'>"+why+"</span></div>";
        return false;
    }
    else
    {
        return true;
    }
}
// function getKey(e){ 
//      alert("2");
//      e = e || window.event; 
//      var keycode = e.which ? e.which : e.keyCode; 
//      if (keycode === 13 || keycode === 108){ //如果按下ENTER键 
//         //在这里设置你想绑定的事件 
//      } 
// } 
 


