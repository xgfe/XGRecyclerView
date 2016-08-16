# XGRecyclerView
A recyclerView library from XGFE Android Components.

##Install

Now just clone this repository and import the module 'xgrecyclerview'.

##Usage

This is a simple list binding object Data to a TextView. Using a XGAdapterHelper , the code is just like this:

```java
recyclerView = (XGRecyclerView) findViewById(R.id.list_act_rv);

adapter = XGAdapter.with(recyclerView.adapter())
        .itemAdapter(XGAdapter
                .<Data>item(R.layout.item_list_with_redtext)
                .bindText(R.id.list_item_tv, data -> data.text)
                .build())
        .build();

adapter.setDataSource(dataSource = new SimpleDataSource<>(adapter));
dataSource.setData(list); //list is where data from
```
Full code is [here](./blob/master/sample/src/main/java/com/xgfe/android/components/xgrecyclerviewsample/ListSample_Simple_Helper_Activity.java).

##SampleList

1. Simple Base List: The basic usage using basic classes. [click](./blob/master/sample/src/main/java/com/xgfe/android/components/xgrecyclerviewsample/ListSample_Simple_Base_Activity.java)
2. Simple Helper List: The basic usage using XGAdapterHelper to build and bind item to datasource. [click](./blob/master/sample/src/main/java/com/xgfe/android/components/xgrecyclerviewsample/ListSample_Simple_Helper_Activity.java)
3. Multiple Type List: The sample for two or multiple types list. [click](./blob/master/sample/src/main/java/com/xgfe/android/components/xgrecyclerviewsample/ListSample_MultipleType_Activity.java)
4. Selectable List: The sample for a list with listener callback and other handler.Include a best practices for ItemAnimator. [click](./blob/master/sample/src/main/java/com/xgfe/android/components/xgrecyclerviewsample/ListSample_Selectable_Activity.java)
5. Collapsible HeaderFooter List: The sample for list using CollapsibleDataSource to be collapsible with header and footer. [click](./blob/master/sample/src/main/java/com/xgfe/android/components/xgrecyclerviewsample/ListSample_Collapsible_Activity.java)
6. Loadable HeaderFooter Filter List: The sample for list using LoadableDataSource to be loadable with header ,footer and filter to filter data. [click](./blob/master/sample/src/main/java/com/xgfe/android/components/xgrecyclerviewsample/ListSample_Loadable_Activity.java)
7. Section HeaderFooter Filter List: The sample for list using SectionDataSource with section titles, header ,footer and filter to filter data. [click](./blob/master/sample/src/main/java/com/xgfe/android/components/xgrecyclerviewsample/ListSample_Section_Activity.java)
8. Menu List: The sample for list using MenuDataSource witch extending from SectionDataSource with click handler. [click](./blob/master/sample/src/main/java/com/xgfe/android/components/xgrecyclerviewsample/ListSample_Menu_Activity.java)
9. Refresh List: The sample just using XGRefresheRecyclerView. [click](./blob/master/sample/src/main/java/com/xgfe/android/components/xgrecyclerviewsample/ListSample_Refresh_Activity.java)
10. TouchHelper List: The sample for list using TouchHelperDataSource to adapt to ItemTouchHelper. [click](./blob/master/sample/src/main/java/com/xgfe/android/components/xgrecyclerviewsample/ListSample_TouchHelper_Activity.java)