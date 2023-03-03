package com.digwex.dagger;

import com.digwex.AppActivity;
import com.digwex.MainActivity;
import com.digwex.MainApplication;

import com.digwex.service.BackgroundService;
import com.digwex.service.MemoryWatcher;
import com.digwex.service.TrafficService;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(
  modules = {
    AppModule.class
  }
)
public interface AppComponent {
  void inject(MainApplication application);

  void inject(BackgroundService service);

  void inject(MainActivity activity);

  void inject(AppActivity appActivity);

  void inject(MemoryWatcher memoryWatcher);

  void inject(TrafficService trafficService);
}
