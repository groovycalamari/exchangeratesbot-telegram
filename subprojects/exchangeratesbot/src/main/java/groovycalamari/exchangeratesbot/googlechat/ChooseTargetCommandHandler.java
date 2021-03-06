/*
 * SPDX-License-Identifier: Apache-2.0
 *
 * Copyright 2020 Sergio del Amo.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package groovycalamari.exchangeratesbot.googlechat;

import edu.umd.cs.findbugs.annotations.NonNull;
import groovycalamari.exchangeratesbot.ChooseTargetCommand;
import groovycalamari.exchangeratesbot.SetTargetCommandHandler;
import io.micronaut.bots.core.ChatBotMessageReceive;
import io.micronaut.bots.core.CommandHandler;
import io.micronaut.bots.googlechat.core.GoogleChatButton;
import io.micronaut.context.annotation.Requires;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Named;
import javax.inject.Singleton;

@Requires(classes = GoogleChatButton.class)
@Named(ChooseTargetCommand.COMMAND_CHOOSETARGET)
@Singleton
public class ChooseTargetCommandHandler extends ChooseCommandHandler {
    private static final Logger LOG = LoggerFactory.getLogger(ChooseTargetCommandHandler.class);

    @NonNull
    @Override
    protected String messageText(@NonNull ChatBotMessageReceive chatUpdate) {
        return "Choose your default target currency";
    }

    @Override
    protected String nextCommand() {
        return CommandHandler.COMMAND_PREFIX + SetTargetCommandHandler.COMMAND_SETTARGET;
    }
}
