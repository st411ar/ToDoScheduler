Ext.onReady(function() {

	var creationFormPanel = new Ext.form.FormPanel({
		url: 'main',
		bodyStyle:'padding:50px 5px 50px',
		frame: true,
        items: [{
            layout:'column',
            border:false,
            items:[{
                columnWidth:.5,
                layout: 'form',
                border:false,
				defaults: {
					anchor:'95%'
				},
                items: [{
					allowBlank:false,
					xtype: 'hidden',
					name: 'action'
                }, {
					allowBlank:false,
					xtype: 'hidden',
					name: 'id'
                }, {
					allowBlank:false,
					xtype: 'datefield',
					name: 'date',
					fieldLabel: 'Дата'
                }, {
					allowBlank:false,
					xtype: 'textfield',
					name: 'name',
					fieldLabel: 'Название'
                }, {
					xtype: 'checkbox',
					name: 'status',
					fieldLabel: 'Сделано'
                }]
            },{
                columnWidth:.5,
                layout: 'form',
                border:false,
				defaults: {
					anchor:'95%'
				},
                items: [{
					allowBlank:false,
                    xtype:'textarea',
                    fieldLabel: 'Описание',
                    name: 'description'
                }]
            }]
        }],
		buttons: [{
			text:'сохранить',
			handler: function() {
				var form = creationFormPanel.getForm();
				if (form.isValid()) {
					form.submit({
						clientValidation: true,
						success: function(form, action) {
							todo.update();
						},
						failure: function(form, action) {
							Ext.Msg.alert('Failure', action.result.msg);
						}
					});
					creationWindow.hide();
				} else {
					Ext.Msg.alert("Сохранение невозможно", "Некоторые поля формы пустые");
				}
			}
		},{
			text:'отменить',
			handler: function() {
				creationWindow.hide();
			}
		}]
	});

	var creationWindow = new Ext.Window({
		width:500,
		height:250,
		layout: 'fit',
		border: false,
		closeAction:'hide',
		items: [creationFormPanel]
	});
	creationWindow.hide();

	var header = new Ext.Panel({
		region: 'north',
		title:	'Планировщик дел'
	});

	var menu = new Ext.tree.TreePanel({
		region:			'west',
		title:			'Меню приложения',
		rootVisible:	false,
		lines:			false,
		width:			200,
		collapsible:	true,
		split:			true,
		minSize:		150,
		maxSize:		400,
	    root: {
	        children: [{
				"id":	"menu-0",
				"text":	"Список дел",
				"leaf":	true
			}, {
				"id":	"menu-1",
				"text":	"История действий",
				"leaf":	true
			}]
	    },

	    select: function(itemIndex) {
			if (itemIndex === 0) {
				this.getNodeById('menu-0').select();
			} else if (itemIndex === 1) {
				this.getNodeById('menu-1').select();
			}
	    }
	});

	var todo = new Ext.grid.GridPanel({
		id: 'tab-0',
		title: 'Список дел',
		tbar: [{
			xtype: 'button',
			iconCls:'add-btn',
			text: 'Создать новое дело',
			handler: function() {
				creationFormPanel.form.reset();
				creationWindow.title = 'Создание нового дела';
				var testData = {
					action: 'create',
					id: 'item-0',
				};
				creationFormPanel.form.setValues(testData);
				creationWindow.show(this);
			}
		},{
			xtype: 'button',
			text: 'Изменить выбранное дело',
			iconCls:'edit-btn',
			handler: function() {
				creationWindow.title = 'Изменение нового дела';
				var selectionModel = todo.getSelectionModel();
				var rowRecord = selectionModel.getSelected();
				if (rowRecord) {
					rowRecord.data.action = 'edit';
					creationFormPanel.form.loadRecord(rowRecord);
					creationWindow.show(this);
				}
			}
		},{
			xtype: 'button',
			iconCls:'delete-btn',
			text: 'Удалить выбранное дело',
			handler: function() {
				var selectionModel = todo.getSelectionModel();
				var rowRecord = selectionModel.getSelected();
				if (rowRecord) {
					rowRecord.data.action = 'delete';
					var form = creationFormPanel.form;
					form.loadRecord(rowRecord);
					form.submit({
						success: function(form, action) {
							todo.update();
						},
						failure: function(form, action) {
							Ext.Msg.alert('Failure', action.result.msg);
						}
					});
				}
			}
		}],
		store: new Ext.data.XmlStore({
			url: 'items',
			record: 'item',
			fields: [
				{
					name:		'id',
					mapping:	'@id'
				},
				'date',
				'name',
				'status',
				'description'
			],
			listeners: {
				'load': function() {
					var selectionModel = todo.getSelectionModel();
					selectionModel.selectRow(0);
				}
			}
		}),
		colModel: new Ext.grid.ColumnModel({
			defaults: {
				menuDisabled: true
			},
			columns: [
				{header: 'Дата', width: 100, dataIndex: 'date'},
				{header: 'Дело', width: 200, dataIndex: 'name'},
				{
					header: 'Статус',
					width: 100,
					dataIndex: 'status',
					renderer: function(value) {
						value = (value === "true" ? "выполнено" : "не выполнено");
						return value;
					}
				},
				{header: 'Описание', width: 500, dataIndex: 'description'}
			]
		}),
		listeners: {
			'rowclick': function(row, rowIndex, e) {
				var selectionModel = this.getSelectionModel();
				selectionModel.clearSelections();
				selectionModel.selectRow(rowIndex);
			}
		},
		update: function() {
			this.store.reload();
		}
	});

	var audit = new Ext.grid.GridPanel({
		id: 'tab-1',
		title: 'История действий',
		store: new Ext.data.XmlStore({
			url: 'audit',
			record: 'action',
			fields: ['time', 'type', 'name']
		}),
		colModel: new Ext.grid.ColumnModel({
			defaults: {
				menuDisabled: true
			},
			columns: [
				{header: 'Сессия пользователя', width: 250, dataIndex: 'time'},
				{
					header: 'Произведенное действие',
					width: 250,
					dataIndex: 'type',
					renderer: function(value) {
						if (value === "create") {
							return "создание";
						} else if (value === "edit") {
							return "изменение";
						} else if (value === "delete") {
							return "удаление";
						}
						return "действие не определено";
					}
				},
				{header: 'Название дела', width: 250, dataIndex: 'name'}
			]
		}),
		update: function() {
			this.store.reload();
		}
	});

	var tabs = new Ext.TabPanel({
		region:		'center',
	    activeTab:	0,
	    items: [todo, audit],

	    select: function(tabIndex) {
	    	if (tabIndex === 0) {
	    		this.activate('tab-0');
	    	} else if (tabIndex === 1) {
	    		this.activate('tab-1');
	    	}

	    }
	});

	menu.on('click', function(node) {
   		if (node.id === 'menu-0') {
   			if (node.isSelected()) {
	    		todo.update();
   			} else {
	   			tabs.select(0);
   			}
   		} else if (node.id === 'menu-1') {
   			if (node.isSelected()) {
	    		audit.update();
   			} else {
	   			tabs.select(1);
   			}
   		}
   	});

   	tabs.on('tabchange', function(tabPanel, tab) {
   		if (tab.id === 'tab-0') {
   			menu.select(0);
    		todo.update();
   		} else if (tab.id === 'tab-1') {
   			menu.select(1);
    		audit.update();
   		}
   	});

	var viewport = new Ext.Viewport({
		layout:	'border',
		items:	[header, menu, tabs]
	});

});