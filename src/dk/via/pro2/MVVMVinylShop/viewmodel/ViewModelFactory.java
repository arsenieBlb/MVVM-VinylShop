package dk.via.pro2.MVVMVinylShop.viewmodel;

import dk.via.pro2.MVVMVinylShop.model.VinylModel;

public class ViewModelFactory
{
  private final VinylListViewModel listViewModel;
  private final VinylAddViewModel addViewModel;

  public ViewModelFactory(VinylModel model)
  {
    this.listViewModel = new VinylListViewModel(model);
    this.addViewModel  = new VinylAddViewModel(model);
  }

  public VinylListViewModel getListViewModel()
  {
    return listViewModel;
  }

  public VinylAddViewModel getAddViewModel()
  {
    return addViewModel;
  }
}