-- +goose Up
-- +goose StatementBegin
CREATE TABLE [dbo].[ViewHistory] (
    [Account] UNIQUEIDENTIFIER NOT NULL CONSTRAINT FK_ViewHistory_Account  FOREIGN KEY REFERENCES [dbo].[Account]([Id]),
    [Episode] UNIQUEIDENTIFIER NOT NULL CONSTRAINT FK_ViewHistory_Episode  FOREIGN KEY REFERENCES [dbo].[Episode]([Id]),
    [CreatedTime] DATETIME,
    [UpdatedTime] DATETIME,
    [Duration] INT NOT NULL CONSTRAINT DF_ViewHistory_Duration DEFAULT 0,
    [IsDeleted] BIT NOT NULL CONSTRAINT DF_ViewHistory_IsDeleted DEFAULT 0
);
-- +goose StatementEnd


-- +goose Down
-- +goose StatementBegin
DROP TABLE [dbo].[ViewHistory];
-- +goose StatementEnd
