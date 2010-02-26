/*
 * Quick and dirty implementation for switching input controls...
 */
var AttachFile = Class.create({
    initialize: function(attachDiv) {
	this.attachDiv = attachDiv ;
        this.fileInput = attachDiv.down('.file-input') ;
        this.urlInput = attachDiv.down('.url-input') ;
        this.fileRadio = attachDiv.down('.file-radio') ;
        this.urlRadio = attachDiv.down('.url-radio') ;
        
        this.urlInput.remove() ;
        this.currInput = this.fileInput ;
        this.fileRadio.observe('click', this._toggleInput.bind(this)) ;
        this.urlRadio.observe('click', this._toggleInput.bind(this))
    },
    
    _toggleInput: function() {
    	if (this.fileRadio.checked && this.currInput !== this.fileInput) {
    		this.urlInput.remove() ;
    		this.attachDiv.insert({top: this.fileInput}) ;
    		this.currInput = this.fileInput ;
    	} else if (this.urlRadio.checked && this.currInput !== this.urlInput) {
    		this.fileInput.remove() ;
    		this.attachDiv.insert({top: this.urlInput}) ;
    		this.currInput = this.urlInput ;
    	}
    }
}) ;
