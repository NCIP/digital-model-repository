/*
 Based on code from:
 http://bosmeeuw.wordpress.com/2009/01/17/easily-repeating-html-form-sections-without-much-javascript/
 Modified:
 	* Entry model and add entry link identified using classes, must be unique with respect to root, which defaults to the document element, but can be specified using the 'root' option
	* Container does not need to be empty, entries are added at the same position as the entry model
	* Updates indexes on entry deletion
	* Name, Id and For templates, templateData option (function executed with 'this' bound to element where template was defined), index key set to current index
	* Hook for executing a function after an entry is added, the added entry is passed as an argument
	* Support for embedding ExpandingFormElements within each other, use afterAdd hook to connect Expander
*/
var ExpandingFormElement = Class.create({
    initialize: function(options) {
        this.options = options ;
		
		var root = $(this.options.root) || $(document.documentElement) ;
		this.entryModel = this._findElement(root, options.entryModelClass) ;
		this.container = this.entryModel.parentNode ;
		this.followingSibling = this.entryModel.next() || null ;
		this.numEntries = 0 ;

        this.entryModel.remove() ;
		
        $(this._findElement(root, options.addEntryLinkClass)).observe('click',this.addEntry.bind(this)) ;
    } ,

    addEntry: function(values) {
        var copiedElement = this.entryModel.clone(true) ;
		
		//Be nice to not have to store templates every time model is cloned, clone does not copy stored data or custom properties...
		this._storeTemplates(copiedElement) ;
		
        this._observeCopiedElement(copiedElement) ;

        var index = this._getNextIndex() ;
		copiedElement.store('expander', this) ;
		copiedElement.store('index', index) ;

		this._insertEntry(copiedElement) ;
		this.numEntries++ ;
		if (this.options.afterAdd) {
			this.options.afterAdd(copiedElement) ;	
		}
		this._processTemplates(copiedElement, index) ;

        if(values != null) {
            this._setEntryValues(copiedElement, values) ;
        }
    } ,

	_storeTemplates: function(element) {
		var descendents = Element.select(element, '*'),
			i = descendents.length ;
		
		while (i--) {
			var curr = descendents[i] ;
			if (curr.hasAttribute('name')) {
				curr.store('name-tpl', new Template(curr.name)) ;
			}
			if (curr.hasAttribute('id')) {
				curr.store('id-tpl', new Template(curr.id)) ;
			}
			if (curr.hasAttribute('for')) {
				curr.store('for-tpl', new Template(curr.htmlFor)) ;
			}
		}
	},

	_insertEntry: function(element) {
		if (this.followingSibling) {
			this.followingSibling.insert({before: element}) ;	
		} else {
			this.container.insert(element) ;
		}
	},

    _setEntryValues: function(element, values) {
       $H(values).each(function(entry) {
          var input = this._getInputFromElementByName(element, entry.key)

          if(input) {
              input.value = entry.value;
          }
       }.bind(this));
    } ,

    _getInputFromElementByName: function(element, name) {
        var matchedInput = null;

        var inputs = element.select('input','textarea','select')

        inputs.each(function(input) {
           if(input.name.indexOf("[" + name + "]") != -1) {
               matchedInput = input;
               return $break;
           }

           return null;
        });

        return matchedInput;
    } ,

    _getNextIndex: function() {
        return this.numEntries ;
    } ,

    _observeCopiedElement: function(element) {
        var deleteEntryElement;

        if((deleteEntryElement = element.down('.' + this.options.deleteEntryElementClass))) {
            deleteEntryElement.observe('click',function() {
				var confirmed = true ;
                if(this.options.deletionConfirmText) {
                    confirmed = confirm(this.options.deletionConfirmText) ;
                }
				if (confirmed) {
					//Re-index following entries...
					var index = element.retrieve('index') ;
					element.nextSiblings().each(function(input) {
						if (input.hasClassName(this.options.entryModelClass)) {
							input.store('index', index) ;
							this._processTemplates(input, index) ;
							index++ ;
						}
					}, this) ;
					element.remove() ;
					this.numEntries-- ;
				}
            }.bind(this))
        }
    } ,

    _processTemplates: function(element, index) {
		var data ;
		if (this.options.templateData) {
			var dataFactory = this.options.templateData.bind(element) ;
			data = dataFactory() ;
		} else {
			data = {} ;
		}
		data.index = index ;
		
		this._processTemplatesRecursively(element.childElements(), data) ;
    },
	
	_processTemplatesRecursively: function(elements, data) {
		var i = elements.length ;
		while (i--) {
			var curr = elements[i] ;
			
			var expander = curr.retrieve('expander') ;
			if (expander) {
				expander._processTemplates(curr, curr.retrieve('index')) ;	
			} else {
				this._processElementTemplates(curr, data) ;
				this._processTemplatesRecursively(curr.childElements(), data) ;
			}
		}
	},
	
	_processElementTemplates: function(element, data) {
		var tpl = null ;
		if ((tpl = element.retrieve('name-tpl'))) {
			element.name = tpl.evaluate(data) ;
		}
		if ((tpl = element.retrieve('id-tpl'))) {
			element.id = tpl.evaluate(data) ;
		}
		if ((tpl = element.retrieve('for-tpl'))) {
			element.htmlFor = tpl.evaluate(data) ;
		}
	},
	
	_findElement: function(root, cssClass) {
        var elems = root.select("." + cssClass) ;
		if (elems.length != 1) {
			throw new Error("ExpandingFormElement: Must have exactly one element with class: " + cssClass + ", found: " + elems.length + ".") ;	
		}
		return elems.first() ;
	}
});

Object.extend(ExpandingFormElement, {
	UID: 1
});