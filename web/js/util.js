function createWindow(id, options){
	var addWidth = 20;
	var addHeight = 0;
	$.Dialog({
        overlay: true,
        overlayClickClose: false,  
        shadow: true,
        flat: false,
        title: options.caption,
        content: '',
        width: options.width+addWidth,
        height: options.height+addHeight,
        onShow: function(_dialog){
            var html = [
                '<iframe scrolling="no" width="'+(options.width+addWidth)+'" height="'+(options.height+addHeight)+'" src="'+options.contentUrl+'" frameborder="0" allowfullscreen="true"></iframe>'
            ].join("");

            $.Dialog.content(html);
        }
    });
}

function getValue(fieldName)	{
	return document.getElementById(fieldName).value;
}

function getFrameContentById(id){
	var selectedWindow = document.getElementById(id+'contentIframe');
	return selectedWindow==null ? null : selectedWindow.contentWindow;
}

function closeWindow(user)	{
	document.getElementById('panelModalLogin').style.display = 'none';
	$.Dialog.close();
}
