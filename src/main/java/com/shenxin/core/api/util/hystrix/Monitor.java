package com.shenxin.core.api.util.hystrix;

import com.netflix.hystrix.metric.consumer.HystrixDashboardStream;
import com.netflix.hystrix.serial.SerialHystrixDashboardData;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerResponse;
import lombok.extern.slf4j.Slf4j;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;

import javax.inject.Named;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @Author: gaobaozong
 * @Description: 监控服务状态
 * @Date: Created in 2017/9/15 - 16:00
 * @Version: V1.0
 */
@Named
@Slf4j
public class Monitor extends AbstractVerticle {
    Observable<String> _stream = HystrixDashboardStream.getInstance().observe().concatMap(new Func1<HystrixDashboardStream.DashboardData, Observable<String>>() {
        public Observable<String> call(HystrixDashboardStream.DashboardData dashboardData) {
            return Observable.from(SerialHystrixDashboardData.toMultipleJsonStrings(dashboardData));
        }
    });

    @Override
    public void start() throws Exception {
        HttpServer server = vertx.createHttpServer();
        server.requestHandler(request -> {
            final AtomicBoolean moreDataWillBeSent = new AtomicBoolean(true);
            HttpServerResponse response = request.response();

            if (request.method() == HttpMethod.GET) {
                response.putHeader("Content-Type", "text/event-stream;charset=UTF-8");
                response.putHeader("Cache-Control", "no-cache, no-store, max-age=0, must-revalidate");
                response.putHeader("Pragma", "no-cache");
                response.putHeader("Access-Control-Allow-Origin", "*");
                response.setChunked(true);

                _stream.subscribe(new Subscriber<String>() {
                    public void onCompleted() {
                        moreDataWillBeSent.set(false);
                    }

                    public void onError(Throwable e) {
                        moreDataWillBeSent.set(false);
                    }

                    public void onNext(String sampleDataAsString) {
                        if (sampleDataAsString != null) {
                            response.write("data: " + sampleDataAsString + "\n\n");
//                            if(!moreDataWillBeSent.get()){response.end();}
//                            moreDataWillBeSent.set(false);
                        }
                    }
                });
            } else {response.setStatusCode(400).end();}
        });
        server.listen(8089);
    }
}
