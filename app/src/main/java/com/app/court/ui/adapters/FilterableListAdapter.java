package com.app.court.ui.adapters;

import android.app.Activity;
import android.widget.Filter;
import android.widget.Filterable;

import com.app.court.ui.viewbinders.abstracts.ViewBinder;
import com.google.common.base.Function;
import com.google.common.base.Strings;

import java.util.ArrayList;
import java.util.List;

public class FilterableListAdapter<T> extends ArrayListAdapter<T> implements Filterable {
	
	protected ArrayList<T> originalList;
	ToStringFilter toStringFilter = new ToStringFilter();
	CharSequence lastFilter = "" ;
	
	public FilterableListAdapter( Activity context, List<T> arrayList, ViewBinder<T> viewBinder, Function<T, String> converter ) {
		super( context, arrayList, viewBinder );
		
		originalList = new ArrayList<T>( arrayList );
		if ( converter != null )
			toStringFilter = new ToStringFilter( converter );
	}
	
	public FilterableListAdapter( Activity context, List<T> arrayList, ViewBinder<T> viewBinder ) {
		super( context, arrayList, viewBinder );
		originalList = new ArrayList<T>( arrayList );
	}
	
	public FilterableListAdapter( Activity context, ViewBinder<T> viewBinder ) {
		super( context, viewBinder );
		originalList = new ArrayList<T>( arrayList );
	}
	
	@Override
	public Filter getFilter() {
		return toStringFilter;
	}
	
	public class ToStringFilter extends Filter {
		
		private CharSequence lastConstrant;
		Function<T, String> converter;
		
		public ToStringFilter( Function<T, String> converter ) {
			this.converter = converter;
		}
		
		public ToStringFilter() {
		}
		
		protected void notifyFilter() {
			filter( lastConstrant );
		}
		
		@Override
		protected FilterResults performFiltering( CharSequence constraint ) {
			this.lastConstrant = constraint;
			FilterResults results = new FilterResults();
			
			if ( Strings.isNullOrEmpty( constraint.toString() ) ) {
				results.count = originalList.size();
				results.values = new ArrayList<T>( originalList );
				return results;
			}
			
			ArrayList<T> filterList = new ArrayList<T>();
			constraint = constraint.toString().toLowerCase();
			for ( int i = 0; i < originalList.size(); i++ ) {
				if ( converter != null ) {
					String apply = converter.apply( originalList.get( i ) );
					if ( apply.toLowerCase().contains( constraint ) ) {
						filterList.add( originalList.get( i ) );
					}
					
				} else if ( originalList.get( i ).toString().toLowerCase()
						.contains( constraint.toString() ) ) {
					filterList.add( originalList.get( i ) );
				}
				
			}
			results.count = filterList.size();
			results.values = filterList;
			return results;
		}
		
		@Override
		protected void publishResults( CharSequence constraint, FilterResults results ) {
			arrayList = (List<T>) results.values;
			notifyDataSetChanged();
		}
	}
	
	@Override
	public void add( T entity ) {
		originalList.add( entity );
		notifyDataSetChanged();
	}
	
	@Override
	public void addAll( List<T> entityList ) {
		originalList.addAll( entityList );
		super.addAll( entityList );
	}
	
	@Override
	public void clearList() {
		originalList.clear();
		super.clearList();
	}
	
	@Override
	public void notifyDataSetChanged() {
		super.notifyDataSetChanged();
	}
	
	public List<T> getOriginalList() {
		return originalList;
	}
	
}
