/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.alipay.sofa.jraft.entity;

import com.alipay.sofa.jraft.Quorum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

/**
 * @author Akai
 */
public final class BallotFactory {
    private static final Logger     LOG                  = LoggerFactory.getLogger(BallotFactory.class);
    private static final String     defaultDecimalFactor = "0.1";
    private static final BigDecimal defaultDecimal       = new BigDecimal(defaultDecimalFactor);

    public static Quorum buildQuorum(Integer readFactor, Integer writeFactor, int size) {
        if (!checkValid(readFactor, writeFactor)) {
            LOG.error("Invalid factor, factor's range must be (0,10)");
            return null;
        }
        if (Objects.isNull(writeFactor)) {
            writeFactor = 10 - readFactor;
        }
        int w = getWriteQuoruum(writeFactor, size);
        return new Quorum(w, size - w + 1);
    }

    public static Quorum buildMajorityQuorum(int size) {
        int majorityQuorum = getMajorityQuorum(size);
        return new Quorum(majorityQuorum, majorityQuorum);
    }

    private static int getWriteQuoruum(int writeFactor, int n) {
        BigDecimal writeFactorDecimal = defaultDecimal.multiply(new BigDecimal(writeFactor))
            .multiply(new BigDecimal(n));
        return writeFactorDecimal.setScale(0, RoundingMode.CEILING).intValue();
    }

    private static int getReadQuorum(int readFactor, int n) {
        int writeQuorum = getWriteQuoruum(10 - readFactor, n);
        return n - writeQuorum + 1;
    }

    private static int getMajorityQuorum(int n) {
        return n / 2 + 1;
    }

    private static boolean checkValid(Integer readFactor, Integer writeFactor) {
        if (Objects.nonNull(readFactor) && Objects.nonNull(writeFactor)) {
            return readFactor + writeFactor == 10 && readFactor > 0 && readFactor < 10 && writeFactor > 0
                   && writeFactor < 10;
        }
        if (Objects.nonNull(readFactor)) {
            return readFactor > 0 && readFactor < 10;
        }
        if (Objects.nonNull(writeFactor)) {
            return writeFactor > 0 && writeFactor < 10;
        }
        LOG.error("When turning on flexible mode, it is necessary to set the value of the read and write factor");
        return false;
    }
}
