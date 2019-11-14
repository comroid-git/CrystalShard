package de.comroid.crystalshard.util.commands.eval.model;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import de.comroid.crystalshard.util.ui.embed.DefaultEmbedFactory;

import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;

import static java.lang.System.nanoTime;

class CompletionViewer {
    private final CompletableFuture<Message> sentResult = new CompletableFuture<>();
    private EvalFactory.Eval eval;
    private CompletionStage<?> evalResult;

    public CompletionViewer(EvalFactory.Eval eval, CompletionStage<?> evalResult) {
        this.eval = eval;
        this.evalResult = evalResult;
    }

    public void handle(EmbedBuilder embed) {
        embed
                .addField("Executed Code", "```javascript\n" + Util.escapeString(eval.getDisplayCode()) + "```")
                .addField("Result", "```" + Util.escapeString(String.valueOf(evalResult)) + "```");
        if (evalResult != null) {
            evalResult.handleAsync((value, throwable) -> {
                sentResult.thenAcceptAsync(message -> {
                    if (message != null) {
                        if (throwable == null) {
                            // finished nicely
                            message.edit(message.getEmbeds()
                                    .get(0)
                                    .toBuilder()
                                    .updateFields((embedField) -> embedField.getName().equals("Result"),
                                            editableEmbedField -> editableEmbedField.setValue("```" + value + "```"))
                                    .addInlineField("Result Completion Time", String.format("```%1.3f ms```", (nanoTime() - this.eval.getStartTime()) / (double) 1000000)))
                                    .join();
                        } else {
                            // exceptionally
                            message.edit(message.getEmbeds()
                                    .get(0)
                                    .toBuilder()
                                    .updateFields((embedField) -> embedField.getName().equals("Result"),
                                            editableEmbedField -> editableEmbedField.setValue("```" + value + "```"))
                                    .addInlineField("Result Completion Time", String.format("```%1.3f ms```", (nanoTime() - this.eval.getStartTime()) / (double) 1000000))
                                    .addField("Result Completion Exception: [" + throwable.getClass().getSimpleName() + "]", "```" + throwable.getMessage() + "```"))
                                    .join();
                        }
                    }
                });

                return null; // nothing we can do at this point
            });
        }
    }

    public boolean complete(Message message) {
        return this.sentResult.complete(message);
    }
}

public class EvalViewer {
    private CompletionViewer viewer;
    private EvalFactory.Eval eval;
    private Object evalResult;
    private Message command;
    private String[] lines;

    public EvalViewer(EvalFactory eval, Message command, String[] lines) {
        this.command = command;
        this.lines = lines;
        try {
            this.eval = eval.prepare(lines);
            this.evalResult = this.eval.run();
        } catch (Throwable e) {
            this.evalResult = e;
        }
    }


    public EmbedBuilder createEmbed(Server server, User user) {
        final EmbedBuilder embedBuilder = DefaultEmbedFactory.create(server, user)
                .setUrl("http://kaleidox.de:8111")
                .setFooter("Evaluated by " + user.getDiscriminatedName());
        if (this.evalResult != null) {
            if (this.evalResult instanceof Throwable) {
                ExecutionFactory.Execution exec = new ExecutionFactory()._safeBuild(lines);
                embedBuilder
                        .addField("Executed Code", "```javascript\n" + Util.escapeString(exec.isVerbose() ? exec.toString() : exec.getOriginalCode()) + "```")
                        .addField("Message of thrown " + this.evalResult.getClass().getSimpleName(), "```" + ((Throwable) this.evalResult).getMessage() + "```");
            } else if (this.evalResult instanceof CompletionStage) {
                this.viewer = new CompletionViewer(this.eval, (CompletionStage<?>) this.evalResult);
                viewer.handle(embedBuilder);
            } else {
                embedBuilder
                        .addField("Executed Code", "```javascript\n" + Util.escapeString(this.eval.getDisplayCode()) + "```")
                        .addField("Result", "```" + Util.escapeString(String.valueOf(evalResult)) + "```");
            }
        }

        embedBuilder
                .addField("Script Time", String.format("```%1.0f ms```", eval.getExecTime()), true)
                .addField("Evaluation Time", String.format("```%1.3f ms```", eval.getEvalTime() / (double) 1000000), true);

        return embedBuilder;
        /*
        DefaultEmbedFactory.create()
                .addField("Executed Code", "```javascript\n" + Util.escapeString(eval.getDisplayCode()) + "```")
                .addField("Result", "```" + Util.escapeString(String.valueOf(evalResult)) + "```")
                .addField("Script Time", String.format("```%1.0fms```", eval.getExecTime()), true)
                .addField("Evaluation Time", String.format("```%1.3fms```", eval.getEvalTime() / (double) 1000000), true)
                .setAuthor(user)
                .setUrl("http://kaleidox.de:8111")
                .setFooter("Evaluated by " + user.getDiscriminatedName())
                .setColor(user.getRoleColor(server).orElse(JamesBot.THEME));
         */
    }

    public boolean complete(Message message) {
        return this.viewer != null && this.viewer.complete(message);
    }
}

 /*
        final CompletableFuture<Message> sentResult = new CompletableFuture<>();

        try {
            HashMap<String, Object> bindings = new HashMap<String, Object>() {{
                put("msg", command);
                put("usr", user);
                put("chl", channel);
                put("srv", server);
                put("api", JamesBot.API);
                put("timer", new Timer());
            }};

            EvalFactory.Eval eval = new EvalFactory(bindings).prepare(lines);
            EvalViewer viewer = new EvalViewer(eval,lines);
            Object evalResult = eval.run();

            if (evalResult instanceof CompletionStage) {
                ((CompletionStage<?>) evalResult).handleAsync((value, throwable) -> {
                    sentResult.thenAcceptAsync(message -> {
                        if (message != null) {
                            if (throwable == null) {
                                // finished nicely
                                message.edit(message.getEmbeds()
                                        .get(0)
                                        .toBuilder()
                                        .addInlineField("Result Completion Time", String.format("```%1.3fms```", (nanoTime() - eval.getStartTime()) / (double) 1000000)))
                                        .join();
                            } else {
                                // exceptionally
                                message.edit(message.getEmbeds()
                                        .get(0)
                                        .toBuilder()
                                        .addField("Result Completion Exception: [" + throwable.getClass().getSimpleName() + "]", "```" + throwable.getMessage() + "```"))
                                        .join();
                            }
                        }
                    });

                    return null; // nothing we can do at this point
                });
            }

            result = DefaultEmbedFactory.create()
                    .addField("Executed Code", "```javascript\n" + Util.escapeString(eval.getDisplayCode()) + "```")
                    .addField("Result", "```" + Util.escapeString(String.valueOf(evalResult)) + "```")
                    .addField("Script Time", String.format("```%1.0fms```", eval.getExecTime()), true)
                    .addField("Evaluation Time", String.format("```%1.3fms```", eval.getEvalTime() / (double) 1000000), true)
                    .setAuthor(user)
                    .setUrl("http://kaleidox.de:8111")
                    .setFooter("Evaluated by " + user.getDiscriminatedName())
                    .setColor(user.getRoleColor(server).orElse(JamesBot.THEME));

            if (evalResult instanceof EmbedBuilder)
                channel.sendMessage((EmbedBuilder) evalResult).join(); // join for handling
        } catch (Throwable t) {
            ExecutionFactory.Execution exec = new ExecutionFactory()._safeBuild(lines);
            result = DefaultEmbedFactory.create()
                    .addField("Executed Code", "```javascript\n" + Util.escapeString(exec.isVerbose() ? exec.toString() : exec.getOriginalCode()) + "```")
                    .addField("Message of thrown " + t.getClass().getSimpleName(), "```" + t.getMessage() + "```")
                    .setAuthor(user)
                    .setUrl("http://kaleidox.de:8111")
                    .setFooter("Evaluated by " + user.getDiscriminatedName())
                    .setColor(user.getRoleColor(server).orElse(JamesBot.THEME));
        }

        if (result != null) {
            channel.sendMessage(result)
                    .thenAccept(sentResult::complete)
                    .thenRun(command::delete)
                    .join();
        }
         */