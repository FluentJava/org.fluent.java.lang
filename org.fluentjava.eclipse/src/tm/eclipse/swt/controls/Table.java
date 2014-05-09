package tm.eclipse.swt.controls;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swtbot.swt.finder.finders.UIThreadRunnable;
import org.eclipse.swtbot.swt.finder.results.Result;
import org.eclipse.swtbot.swt.finder.results.VoidResult;

import tm.eclipse.Plugin_Config;
import tm.eclipse.swt.Control_Get;
import tm.eclipse.swt.Control_Self;
import tm.eclipse.swt.Control_Set;

public class Table extends org.eclipse.swt.widgets.Table
{
	public Display 			   display;
	public Composite 		   target;
	public Control_Set<Table>  set;
	public Control_Get<Table>  get;
	public Control_Self<Table>  self;
	//callbacks	
	public TableResize		   resize; 

	public Table(Composite parent)
	{
		this(parent, SWT.MULTI | SWT.BORDER | SWT.FULL_SELECTION);
	}
	public Table(Composite parent, int style) 
	{
		super(parent, style);
		this.display = parent.getDisplay();			// we need to store this in case there are multiple ones
		this.target  = parent;	
		this.set     = new Control_Set <Table>(this);
		this.get     = new Control_Get <Table>(this);
		this.self    = new Control_Self<Table>(this);
		this.resize  = new TableResize();
		
		this.setLinesVisible (true);
		this.setHeaderVisible (true);
	}

	public class TableResize implements Runnable
	{
		public void run()
		{
			Table.this.add_TableColumn("test",null);
		}
	}
	
	public static Table add_Table(Composite target)
	{
		return add_Table(target, SWT.MULTI | SWT.BORDER | SWT.FULL_SELECTION);
	}
	
	public static Table add_Table(final Composite target, final int style)
	{	
		if (target == null)
			return null;			
		return UIThreadRunnable.syncExec(target.getDisplay(),new Result<Table>() { public Table run() 
					{
						Table table = new Table(target,style);												
						return table.refresh();						
					}});		
	} 
		
	public TableColumn add_TableColumn(final String columnName, final Image image)
	{
		return add_TableColumn(columnName, image,Plugin_Config.SWT_BEST_MAX_TABLE_COLUMN_WIDTH);
	}
	public TableColumn add_TableColumn(final String columnName, final Image image, final int width)
	{
		return UIThreadRunnable.syncExec(display,new Result<TableColumn>() { public TableColumn run() 
			{		
				TableColumn column = new TableColumn (Table.this, SWT.NONE);				
				if (columnName != null)
					column.setText (columnName);
				if (image != null)
					column.setImage(image);
				column.setMoveable(true);
				if (width > -1)
				column.setWidth(width);			
				else
					column.pack();
				
				return column;			
			}});			
	}
	public TableItem add_TableItem(final List<Object>  values)
	{
		return add_TableItem(values.toArray(new Object[] {}));
	}
	public TableItem add_TableItem(final Object ... values)
	{
		return UIThreadRunnable.syncExec(display,new Result<TableItem>() { public TableItem run() 
			{
				TableItem item = new TableItem (Table.this, SWT.NONE);
				for(int i=0; i < values.length; i ++ )
				{		
					String text = values[i] == null ? "" : values[i].toString();
					item.setText(i, text);					
				}
				return item; 
			}});
	}
	public List<TableItem> items()
	{
		return UIThreadRunnable.syncExec(display,new Result<List<TableItem>>() { public List<TableItem> run() 
			{
				return Arrays.asList(Table.this.getItems());
			}});
	}
	
	public Table items_Clear()
	{		
		UIThreadRunnable.syncExec(display,new VoidResult() { public void run() 
			{
				for(TableItem item : items())
				{
					item.dispose();	
				}
			}});
		return this;
	}
	public List<TableColumn> columns()
	{
	
		return UIThreadRunnable.syncExec(display,new Result<List<TableColumn>>() { public List<TableColumn> run() 
			{
				return Arrays.asList(Table.this.getColumns());
			}});
	}	
	
	public Table column_Width(final int columnIndex, final int newWidth)
	{
		UIThreadRunnable.syncExec(display,new VoidResult() { public void run() 
		{
			Table.this.columns().get(columnIndex).setWidth(newWidth);
		}});
	return this;
	}
	
	public Table columns(List<String> columnNames)
	{
		return columns(columnNames.toArray(new String[] {}));
	}
	public Table columns(String ... columnNames)
	{
		for(String columnName : columnNames)
			add_TableColumn(columnName, null);
		return this;
	}
	public List<String> columns_Names()
	{
		List<String> columnsNames = new ArrayList<String>();
		for(TableColumn column : columns())			
			columnsNames.add(column.getText());
		return columnsNames;
	}
	public Table columns_Clear()
	{
		UIThreadRunnable.syncExec(display,new VoidResult() { public void run() 
			{
				for(TableColumn column: columns())
					column.dispose();
			}});
		return this;
	}
	public Table column_Clear(final int columnIndex)
	{
		UIThreadRunnable.syncExec(display,new VoidResult() { public void run() 
			{
				List<TableColumn> columns = columns();
				if (columns.size() > columnIndex)
					columns.get(columnIndex).dispose();
			}});
		return this;
	}
	public Table add_Row(Object ... values)
	{
		add_TableItem(values);
		return this;
	}
	public Table item(Object ... values)
	{
		add_TableItem(values);
		return this;
	}
	public Table item(List<String> values)
	{
		return item(values.toArray(new String[] {}));
	}

	public Table refresh()
	{
		if (target != null && display != null)			
			UIThreadRunnable.syncExec(display,new VoidResult() { public void run() 
			{				
				target.layout(true);
			}});
					
		return this;
	}
	public Table fill()
	{
		return set.layout.grid_Fill();
	}
	public Table fill(int horizontalSpan)
	{
		return set.layout.grid_Grab_All(horizontalSpan, 1);
	}
	public int column_Best_Width(final TableColumn column)
	{		
		return UIThreadRunnable.syncExec(display,new Result<Integer>() { public Integer run() 
		{
			int bestWidth = Plugin_Config.SWT_BEST_MAX_TABLE_COLUMN_WIDTH;
			if (column.getWidth() > bestWidth)
				return bestWidth;
			bestWidth = column.getWidth();
			//column.
			//Table.this.getItems()[0].get
			//for()
			return bestWidth;
		}});
		
	}
	protected void checkSubclass()
	{}
}
