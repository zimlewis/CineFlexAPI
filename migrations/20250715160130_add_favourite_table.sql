-- +goose Up
-- +goose StatementBegin
CREATE TABLE [dbo].[Favorite] (
    [Show] UNIQUEIDENTIFIER NOT NULL FOREIGN KEY REFERENCES [dbo].[Show]([Id]),
    [Account] UNIQUEIDENTIFIER NOT NULL FOREIGN KEY REFERENCES [dbo].[Account]([Id]),
    PRIMARY KEY ([Show], [Account]),
    [CreatedTime] DATETIME
);
-- +goose StatementEnd

-- +goose Down
-- +goose StatementBegin
DROP TABLE [dbo].[Favorite];
-- +goose StatementEnd
