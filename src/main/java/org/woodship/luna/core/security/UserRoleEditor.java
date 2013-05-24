package org.woodship.luna.core.security;

import java.util.LinkedHashSet;
import java.util.Set;

import org.woodship.luna.util.Utils;

import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.JPAContainerItem;
import com.vaadin.data.Item;
import com.vaadin.shared.ui.MultiSelectMode;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

@SuppressWarnings("serial")
public class UserRoleEditor extends Window  {
	private JPAContainerItem<Role> jpaitem = null;
	private Table table;
	@SuppressWarnings("unchecked")
	public UserRoleEditor(final Item item,  final JPAContainer<User> container, final JPAContainer<Role> roleContainer) {
		this.setCaption("为用户设置角色");
		this.jpaitem = (JPAContainerItem<Role>) item;
		final VerticalLayout formLayout = new VerticalLayout();
		formLayout.setMargin(true);
		table = new Table();
		table.setContainerDataSource(roleContainer);
		table.setVisibleColumns(new Object[]{"username","showName"});
		Utils.setTableCaption(table, User.class);
		table.setWidth(300, Unit.PIXELS);
		table.setHeight(400, Unit.PIXELS);
		table.setMultiSelect(true);
		table.setSelectable(true);
		table.setMultiSelectMode(MultiSelectMode.SIMPLE);
		//设置值
		Role r = jpaitem.getEntity();
		for(User u : r.getUsers()){
			table.select(u.getId());
		}
		
		
		formLayout.addComponent(table);
		final Label error = new Label("", ContentMode.HTML);
		error.setVisible(false);
		formLayout.addComponent(error);


		//处理保存事件
		Button saveButton = new Button("保存");
		saveButton.addClickListener(new Button.ClickListener() {
			@Override
			public void buttonClick(Button.ClickEvent event) {
				Set<?> v = (Set<?>) table.getValue();
				Set<Role> rolses = new LinkedHashSet<Role>();
				for(Object rid : v){
					rolses.add(roleContainer.getItem(rid).getEntity() );
				}
				//TODO 有问题
				jpaitem.getItemProperty("rolses").setValue(rolses);
				container.commit();
				Notification.show("保存成功");
			}
		});
		
		HorizontalLayout buttons = new HorizontalLayout();
		buttons.setMargin(true);
		buttons.addComponent(saveButton);
		formLayout.addComponent(buttons);
		formLayout.setComponentAlignment(buttons, Alignment.MIDDLE_LEFT);
		setContent(formLayout);
	}
	

}
