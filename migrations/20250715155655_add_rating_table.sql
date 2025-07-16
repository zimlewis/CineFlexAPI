-- +goose Up
-- +goose StatementBegin
CREATE TABLE [dbo].[Rating] (
    [Account] UNIQUEIDENTIFIER NOT NULL FOREIGN KEY REFERENCES [dbo].[Account]([Id]),
    [Show] UNIQUEIDENTIFIER NOT NULL FOREIGN KEY REFERENCES [dbo].[Show]([Id]),
    PRIMARY KEY ([Account], [Show]),
    [Value] INT NOT NULL,
    [CreatedTime] DATETIME,
    [UpdatedTime] DATETIME,
    [IsDeleted] BIT NOT NULL DEFAULT 0
);
-- +goose StatementEnd

-- +goose Down
-- +goose StatementBegin
DROP TABLE [dbo].[Rating];
-- +goose StatementEnd
